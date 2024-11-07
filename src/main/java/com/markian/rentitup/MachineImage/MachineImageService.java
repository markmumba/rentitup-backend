package com.markian.rentitup.MachineImage;

import com.markian.rentitup.Exceptions.MachineImageException;
import com.markian.rentitup.MachineImage.MachineImageDto.MachineImageResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MachineImageService {
    String addImages(Long machineId, MultipartFile[] images) throws MachineImageException;

    List<MachineImageResponseDto> getAllMachineImages(Long machineId) throws MachineImageException;

    MachineImageResponseDto getMachineImage(Long machineId, Long imageId) throws MachineImageException;

    String deleteMachineImage(Long machineId, Long imageId) throws MachineImageException;

    void setIsPrimary(Long machineId, Long imageId) throws MachineImageException;
}
