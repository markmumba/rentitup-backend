package com.markian.rentitup.User.UserDto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String role;
}
