package com.markian.rentitup.MachineImage.Impl;

import com.markian.rentitup.Exceptions.MachineImageException;
import com.markian.rentitup.Machine.Machine;
import com.markian.rentitup.Machine.MachineRepository;
import com.markian.rentitup.MachineImage.MachineImage;
import com.markian.rentitup.MachineImage.MachineImageDto.MachineImageMapper;
import com.markian.rentitup.MachineImage.MachineImageDto.MachineImageResponseDto;
import com.markian.rentitup.MachineImage.MachineImageRepository;
import com.markian.rentitup.MachineImage.MachineImageService;
import com.markian.rentitup.Utils.AwsS3Service;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class MachineImageServiceImpl implements MachineImageService {

    private final AwsS3Service awsS3Service;
    private final MachineRepository machineRepository;
    private final MachineImageRepository machineImageRepository;
    private final MachineImageMapper machineImageMapper;

    public MachineImageServiceImpl(AwsS3Service awsS3Service, MachineRepository machineRepository, MachineImageRepository machineImageRepository, MachineImageMapper machineImageMapper) {
        this.awsS3Service = awsS3Service;
        this.machineRepository = machineRepository;
        this.machineImageRepository = machineImageRepository;
        this.machineImageMapper = machineImageMapper;
    }


    @Override
    @Transactional
    public String addImages(Long machineId, MultipartFile[] images) throws MachineImageException {
        try {
            Machine machine = machineRepository.findById(machineId).orElseThrow(
                    () -> new MachineImageException("Unable to find machine with id " + machineId)
            );

            if (images == null || images.length == 0) {
                throw new MachineImageException("No images provided");
            }

            boolean hasPrimaryImage = machineImageRepository.existsByMachineIdAndIsPrimaryTrue(machineId);

            List<MachineImage> savedImages = new ArrayList<>();
            boolean hasNoImages = !machineImageRepository.existsByMachineId(machineId);

            for (int i = 0; i < images.length; i++) {
                MultipartFile image = images[i];

                try {
                    String contentType = image.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        throw new MachineImageException("File must be an image");
                    }

                    MachineImage machineImage = new MachineImage();

                    String imageUrl = awsS3Service.saveImageToS3(image,"machineImage/");
                    machineImage.setMachine(machine);
                    machineImage.setUrl(imageUrl);

                    machineImage.setIsPrimary(i == 0 && !hasPrimaryImage);
                    // Save to database
                    MachineImage savedImage = machineImageRepository.save(machineImage);
                    savedImages.add(savedImage);

                } catch (Exception e) {
                    rollbackUploadedImages(savedImages);
                    throw new MachineImageException("Failed to upload image: " + e.getMessage(), e);
                }
            }

            return String.format("Successfully uploaded %d images", savedImages.size());

        } catch (MachineImageException e) {
            throw e;
        } catch (Exception e) {
            throw new MachineImageException("Could not add images: " + e.getMessage(), e);
        }
    }


    private void rollbackUploadedImages(List<MachineImage> savedImages) {
        for (MachineImage image : savedImages) {
            try {
                awsS3Service.deleteImageFromS3(image.getUrl());
                machineImageRepository.delete(image);
            } catch (Exception e) {
                throw new MachineImageException(e.getMessage());
            }
        }
    }


    @Override
    public List<MachineImageResponseDto> getAllMachineImages(Long machineId) throws MachineImageException {
        try {
            return machineImageRepository.findAllByMachineId(machineId)
                    .stream()
                    .map(machineImageMapper::fromEntity)
                    .toList();
        } catch (Exception e) {
            throw new MachineImageException("no machine images for machine");
        }
    }

    @Override
    public MachineImageResponseDto getMachineImage(Long machineId, Long imageId) throws MachineImageException {
        return
                machineImageMapper.fromEntity(
                        machineImageRepository.findByMachineIdAndId(machineId, imageId)
                                .orElseThrow(() -> new MachineImageException(
                                        String.format("Image with id %d not found for machine %d", imageId, machineId)
                                )));
    }


    @Override
    public String deleteMachineImage(Long machineId, Long imageId) throws MachineImageException {
        MachineImage machineImage = machineImageRepository.findByMachineIdAndId(machineId, imageId)
                .orElseThrow(() -> new MachineImageException(
                        String.format("Image with id %d not found for machine %d", imageId, machineId)
                ));

        try {
            awsS3Service.deleteImageFromS3(machineImage.getUrl());
            machineImageRepository.delete(machineImage);
            return "Image deleted successfully";
        } catch (Exception e) {
            throw new MachineImageException("Failed to delete image file: " + e.getMessage());

        }
    }

    @Override
    public void setIsPrimary(Long machineId, Long imageId) throws MachineImageException {
        try {
            machineImageRepository.updateAllPrimaryToFalse(machineId);
            MachineImage image = machineImageRepository.findById(imageId)
                    .orElseThrow(() -> new MachineImageException("Image not found with id: " + imageId));

            if (!image.getMachine().getId().equals(machineId)) {
                throw new MachineImageException("Image does not belong to the specified machine");
            }
            image.setIsPrimary(true);
            machineImageRepository.save(image);

        } catch (Exception e) {
            throw new MachineImageException("Failed to update to isprimary " + e.getMessage());
        }
    }
}
