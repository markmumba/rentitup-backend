package com.markian.rentitup.User.UserDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.markian.rentitup.Machine.MachineDto.MachineListResponseDto;
import com.markian.rentitup.Machine.MachineDto.MachineResponseDto;
import com.markian.rentitup.User.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private String profileImage;
    private Role role;
    private LocalDateTime createdAt;
    private String verificationImage;
    private Boolean verified;
    private String registrationId;
    private LocalDateTime verifiedAt;
    private List<MachineResponseDto> ownedMachines;

}
