package com.markian.rentitup.MachineImage.Impl;

import com.markian.rentitup.Exceptions.MachineImageException;
import com.markian.rentitup.Machine.Machine;
import com.markian.rentitup.Machine.MachineRepository;
import com.markian.rentitup.MachineImage.AwsS3Service;
import com.markian.rentitup.MachineImage.MachineImage;
import com.markian.rentitup.MachineImage.MachineImageRepository;
import com.markian.rentitup.MachineImage.MachineImageService;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class MachineImageServiceImpl implements MachineImageService {

    private final AwsS3Service awsS3Service;
    private final MachineRepository machineRepository;
    private final MachineImageRepository machineImageRepository;

    public MachineImageServiceImpl(AwsS3Service awsS3Service, MachineRepository machineRepository, MachineImageRepository machineImageRepository) {
        this.awsS3Service = awsS3Service;
        this.machineRepository = machineRepository;
        this.machineImageRepository = machineImageRepository;
    }


    @Override
    @Transactional
    public String addImages(Long machineId, MultipartFile[] images) throws MachineImageException {
        try {
            String imageUrl = null;
            Machine machine = machineRepository.findById(machineId).orElseThrow(
                    () -> new MachineImageException("Unable to find machine with id " + machineId)
            );

            if (images == null || images.length == 0) {
                throw new MachineImageException("No images provided");
            }


            List<MachineImage> savedImages = new ArrayList<>();

            boolean hasNoImages = !machineImageRepository.existsByMachineId(machineId);

            MachineImage machineImage = new MachineImage();

            for (MultipartFile image : images) {
                try {
                    imageUrl = awsS3Service.saveImageToS3(image);
                    machineImage.setMachine(machine);
                    machineImage.setUrl(imageUrl);
                    machineImage.setIsPrimary(hasNoImages && savedImages.isEmpty());

                    savedImages.add(machineImageRepository.save(machineImage));

                } catch (Exception e) {
                    rollbackUploadedImages(savedImages);
                    throw new MachineImageException("Failed to upload image: " + e.getMessage(), e);

                }
            }

            return "Images saved successfully";

        } catch (MachineImageException e) {
            throw e;
        } catch (Exception e) {
            throw new MachineImageException("could not add images " + e.getMessage(), e);
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
    public List<MachineImage> getAllMachineImages(Long machineId) throws MachineImageException {
        try {
            return machineImageRepository.findAllByMachineId(machineId);
        } catch (Exception e) {
            throw new MachineImageException("no machine images for machine");
        }
    }

    @Override
    public MachineImage getMachineImage(Long machineId, Long imageId) throws MachineImageException {
        return machineImageRepository.findByMachineIdAndImageId(machineId, imageId)
                .orElseThrow(() -> new MachineImageException(
                        String.format("Image with id %d not found for machine %d", imageId, machineId)
                ));
    }


    @Override
    public String deleteMachineImage(Long machineId, Long imageId) throws MachineImageException {
        MachineImage machineImage = machineImageRepository.findByMachineIdAndImageId(machineId, imageId)
                .orElseThrow(() -> new MachineImageException(
                        String.format("Image with id %d not found for machine %d", imageId, machineId)
                ));
        if (machineImage.getIsPrimary()) {
            throw new MachineImageException("You cannot delete the primary image ");
        }
        try {
            awsS3Service.deleteImageFromS3(machineImage.getUrl());
            machineImageRepository.delete(machineImage);
            return "Image deleted successfully";
        } catch (Exception e) {
            throw new MachineImageException("Failed to delete image file: " + e.getMessage());

        }
    }
}
