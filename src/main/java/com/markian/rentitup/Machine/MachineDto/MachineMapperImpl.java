package com.markian.rentitup.Machine.MachineDto;

import com.markian.rentitup.Machine.Machine;
import com.markian.rentitup.Machine.MachineCondition;
import com.markian.rentitup.MachineImage.MachineImage;
import com.markian.rentitup.User.Role;
import com.markian.rentitup.User.User;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MachineMapperImpl implements MachineMapper {


    @Data
    public static class UserSimpleDto {
        private Long id;
        private String email;
        private String fullName;
        private String phone;
        private Role role;
        private LocalDateTime verifiedAt;
    }

    @Data
    public static class MachineImageDto {
        private Long id;
        private String url;
        private Boolean isPrimary;
    }

    public MachineImageDto fromEntity(MachineImage machineImage) {
        MachineImageDto responseDto = new MachineImageDto();
        responseDto.setId(machineImage.getId());
        responseDto.setUrl(machineImage.getUrl());
        responseDto.setIsPrimary(machineImage.getIsPrimary());
        return responseDto;
    }

    public Machine toEntity(MachineRequestDto dto) {
        Machine machine = new Machine();
        machine.setName(dto.getName());
        machine.setDescription(dto.getDescription());
        machine.setBasePrice(dto.getBasePrice());
        machine.setSpecification(dto.getSpecification());
        machine.setCondition(MachineCondition.valueOf(dto.getCondition()));
        return machine;
    }

    public MachineResponseDto toResponseDto(Machine machine) {
        MachineResponseDto responseDto = new MachineResponseDto();
        responseDto.setId(machine.getId());
        responseDto.setName(machine.getName());
        responseDto.setDescription(machine.getDescription());
        responseDto.setBasePrice(machine.getBasePrice());
        responseDto.setSpecification(machine.getSpecification());
        responseDto.setIsAvailable(machine.getIsAvailable());
        responseDto.setCondition(machine.getCondition());
        responseDto.setVerified(machine.getVerified());
        if (machine.getOwner() != null) {
            responseDto.setOwner(toSimpleUserDto(machine.getOwner()));
        }
        if (machine.getMachineImages() != null) {
            responseDto.setMachineImages(
                    machine.getMachineImages().stream()
                            .map(this::fromEntity)
                            .toList()
            );
        }
        if (machine.getCategory() != null) {
            responseDto.setCategoryId(machine.getCategory().getId());
        }

        return responseDto;
    }

    public MachineListResponseDto toListResponseDto(Machine machine) {
        MachineListResponseDto responseDto = new MachineListResponseDto();
        responseDto.setId(machine.getId());
        responseDto.setName(machine.getName());
        responseDto.setBasePrice(machine.getBasePrice());
        responseDto.setIsAvailable(machine.getIsAvailable());
        responseDto.setCondition(machine.getCondition());
        responseDto.setDescription(machine.getDescription());
        responseDto.setVerified(machine.getVerified());
        responseDto.setMachineImageUrl(machine.getMachineImages()
                .stream()
                .filter(MachineImage::getIsPrimary)
                .map(MachineImage::getUrl)
                .findFirst()
                .orElse(null));


        if (machine.getCategory() != null) {
            responseDto.setCategoryId(machine.getCategory().getId());
        }


        return responseDto;
    }

    private UserSimpleDto toSimpleUserDto(User user) {
        UserSimpleDto dto = new UserSimpleDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setVerifiedAt(user.getVerifiedAt());
        return dto;
    }
}