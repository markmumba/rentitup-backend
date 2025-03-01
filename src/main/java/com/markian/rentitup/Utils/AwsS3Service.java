package com.markian.rentitup.Utils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.markian.rentitup.Exceptions.UserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@Service
public class AwsS3Service {

    @Value("${bucket-name}")
    private String bucketName;

    @Value("${aws.s3.access-key}")
    private String awsS3AccessKey;

    @Value("${aws.s3.secret-key}")
    private String awsS3SecretKey;

    public String saveImageToS3(MultipartFile photo, String foldername) {
        // First check file size
        if (photo.getSize() > 10 * 1024 * 1024) {
            throw new UserException("Image size exceeds 10MB limit");
        }

        try {
            String s3Filename = foldername + UUID.randomUUID() + "_" + photo.getOriginalFilename();

            // Create S3 client once and reuse it (consider making it a bean)
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.US_EAST_2)
                    .build();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(photo.getContentType());
            metadata.setContentLength(photo.getSize());

            // Let AWS detect content type instead of hardcoding to JPEG
            metadata.setContentType(photo.getContentType());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3Filename, photo.getInputStream(), metadata);
            s3Client.putObject(putObjectRequest);

            return "https://" + bucketName + ".s3.amazonaws.com/" + s3Filename;

        } catch (IOException e) {
            throw new UserException("Failed to read image file: " + e.getMessage(), e);
        } catch (AmazonS3Exception e) {
            throw new UserException("Failed to upload to S3: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new UserException("Unexpected error during S3 upload: " + e.getMessage(), e);
        }
    }
    public void deleteImageFromS3(String imageUrl) {
        try {
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.US_EAST_2)
                    .build();


            String s3Filename = extractS3KeyFromUrl(imageUrl);
            boolean exists = s3Client.doesObjectExist(bucketName, s3Filename);

            if (!exists) {
                throw new UserException("Image file does not exist in S3: " + s3Filename);
            }

            try {
                ObjectMetadata metadata = s3Client.getObjectMetadata(bucketName, s3Filename);
            } catch (AmazonS3Exception e) {
                throw e;
            }

            s3Client.deleteObject(bucketName, s3Filename);

        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 403) {
                throw new UserException("Permission denied: Unable to delete image from S3. Please check IAM permissions.", e);
            }
            throw new UserException("AWS S3 error while deleting image: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new UserException("Unexpected error while deleting image from S3: " + e.getMessage(), e);
        }
    }

    private String extractS3KeyFromUrl(String imageUrl) {
        try {
            String baseUrl = "https://" + bucketName + ".s3.amazonaws.com/";
            if (imageUrl.startsWith(baseUrl)) {
                return imageUrl.substring(baseUrl.length());
            }
            URL url = new URL(imageUrl);
            String path = url.getPath();
            return path.startsWith("/") ? path.substring(1) : path;
        } catch (MalformedURLException e) {
            throw new UserException("Invalid S3 URL format: " + imageUrl);
        }
    }
}

