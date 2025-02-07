package com.markian.rentitup.User.UserDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetConfirmReset {
    private String token;
    private String newPassword;
}
