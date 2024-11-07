package com.markian.rentitup.MachineImage.MachineImageDto;

import com.markian.rentitup.MachineImage.MachineImage;
import org.springframework.stereotype.Service;

@Service
public class MachineImageMapper {

    public MachineImageResponseDto fromEntity(MachineImage machineImage) {
        MachineImageResponseDto responseDto = new MachineImageResponseDto();
        responseDto.setId(machineImage.getId());
        responseDto.setUrl(machineImage.getUrl());
        responseDto.setIsPrimary(machineImage.getIsPrimary());
        return responseDto;
    }

}
