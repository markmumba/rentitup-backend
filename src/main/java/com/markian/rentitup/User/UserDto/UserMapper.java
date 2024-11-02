package com.markian.rentitup.User.UserDto;

import com.markian.rentitup.Machine.MachineDto.MachineMapper;
import com.markian.rentitup.Machine.MachineDto.MachineResponseDto;
import com.markian.rentitup.User.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        user.setRole(dto.getRole());
        return user;
    }

    public UserResponseDto toResponseDto(User user) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(user.getId());
        responseDto.setEmail(user.getEmail());
        responseDto.setFullName(user.getFullName());
        responseDto.setPhone(user.getPhone());
        responseDto.setRole(user.getRole());
        responseDto.setVerifiedAt(user.getVerifiedAt());

        if (user.getOwnedMachines() != null && !user.getOwnedMachines().isEmpty()) {
            List<MachineResponseDto> machineResponseDtos = user.getOwnedMachines().stream()
                    .map(machineMapper::toResponseDto)
                    .collect(Collectors.toList());
            responseDto.setOwnedMachines(machineResponseDtos);
        }

        return responseDto;
    }

    public UserListResponseDto toListResponseDto(User user) {
        UserListResponseDto responseDto = new UserListResponseDto();
        responseDto.setId(user.getId());
        responseDto.setEmail(user.getEmail());
        responseDto.setFullName(user.getFullName());
        responseDto.setRole(user.getRole());
        responseDto.setVerified(user.getVerifiedAt() != null);
        return responseDto;
    }
}
