package com.markian.rentitup.User.UserDto;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class AuthResponse {
    private UserDetails userDetails;
    private String token;
}
