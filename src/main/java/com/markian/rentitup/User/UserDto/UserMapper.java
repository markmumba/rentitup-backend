package com.markian.rentitup.User.UserDto;

import com.markian.rentitup.Machine.MachineDto.MachineMapper;
import com.markian.rentitup.User.Role;
import com.markian.rentitup.User.User;
import org.springframework.stereotype.Service;


@Service
public class UserMapper {

    private final MachineMapper machineMapper;

    public UserMapper(MachineMapper machineMapper) {
        this.machineMapper = machineMapper;
    }

    public User toEntity(UserRequestDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setRole(Role.valueOf(dto.getRole()));
        return user;
    }

    public UserResponseDto toResponseDto(User user) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(user.getId());
        responseDto.setEmail(user.getEmail());
        responseDto.setFullName(user.getFullName());
        responseDto.setPhone(user.getPhone());
        responseDto.setProfileImage(user.getProfileImageUrl());
        responseDto.setRole(user.getRole());
        responseDto.setCreatedAt(user.getCreatedAt());
        responseDto.setVerificationImage(user.getVerificationImageUrl());
        responseDto.setVerified(user.getVerified());
        responseDto.setRegistrationId(user.getRegistrationId());
        responseDto.setVerifiedAt(user.getVerifiedAt());
        if (user.getOwnedMachines() != null) {
            responseDto.setOwnedMachines(
                    user.getOwnedMachines().stream()
                            .map(machineMapper::toResponseDto)
                            .toList()
            );
        }

        return responseDto;
    }

    public UserListResponseDto toListResponseDto(User user) {
        UserListResponseDto responseDto = new UserListResponseDto();
        responseDto.setId(user.getId());
        responseDto.setEmail(user.getEmail());
        responseDto.setFullName(user.getFullName());
        responseDto.setRole(user.getRole());
        return responseDto;
    }
}
