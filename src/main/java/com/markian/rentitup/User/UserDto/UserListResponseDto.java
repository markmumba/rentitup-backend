package com.markian.rentitup.User.UserDto;


import com.markian.rentitup.User.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponseDto {
    private Long id;
    private String email;
    private String fullName;
    private Role role;
    private boolean isVerified;
}
