package com.markian.rentitup.User.UserDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.markian.rentitup.Machine.MachineDto.MachineResponseDto;
import com.markian.rentitup.User.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private Role role;
    private LocalDate verifiedAt;
    private List<MachineResponseDto> ownedMachines;
}
