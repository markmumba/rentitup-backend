package com.markian.rentitup.MachineImage;

import com.markian.rentitup.Exceptions.MachineImageException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MachineImageService {
    String addImages(Long machineId, MultipartFile[] images) throws MachineImageException;

    List<MachineImage> getAllMachineImages(Long machineId) throws MachineImageException;

    MachineImage getMachineImage(Long machineId, Long imageId) throws MachineImageException;

    String deleteMachineImage(Long machineId, Long imageId) throws MachineImageException;
}
