package com.markian.rentitup.User;

import com.markian.rentitup.User.UserDto.UserRequestDto;
import com.markian.rentitup.User.UserDto.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser (
            @RequestBody UserRequestDto userRequestDto
            )
    {
        UserResponseDto userResponseDto = userService.registerUser(userRequestDto);
        return ResponseEntity.ok(userResponseDto);
    }
}