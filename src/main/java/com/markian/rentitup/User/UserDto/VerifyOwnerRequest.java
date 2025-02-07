package com.markian.rentitup.User.UserDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOwnerRequest {
    private Long id;
    private boolean status;
}