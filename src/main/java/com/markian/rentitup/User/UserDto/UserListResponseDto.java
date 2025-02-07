package com.markian.rentitup.User.UserDto;


import com.markian.rentitup.User.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponseDto {
    private Long id;
    private String email;
    private String fullName;
    private Role role;
}
