package com.markian.rentitup.User;

import com.markian.rentitup.User.UserDto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordResetService passwordResetService;

    public AuthController(UserService userService, PasswordResetService passwordResetService) {
        this.userService = userService;
        this.passwordResetService = passwordResetService;
    }


    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(
            @RequestBody UserRequestDto userRequestDto
    ) {
        UserResponseDto userResponseDto = userService.registerUser(userRequestDto);
        return ResponseEntity.ok(userResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(
            @RequestBody LoginRequest loginRequest
    ) {
        AuthResponse authResponse = userService.login(loginRequest);
        return ResponseEntity.ok(authResponse);
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestBody PasswordResetRequest passwordResetRequest
    ) {
        String response = passwordResetService.initiatePasswordReset(passwordResetRequest.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword (
            @RequestBody PasswordResetConfirmReset passwordResetConfirmReset )
    {
        String response = passwordResetService.confirmPasswordReset(
                passwordResetConfirmReset.getToken(),
                passwordResetConfirmReset.getNewPassword()
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping("/upload-verification")
    public ResponseEntity<String> uploadVerification(
            @RequestParam("file") MultipartFile file,
            @RequestParam("registrationId") String registrationId,
            @RequestParam("nationalId") String nationalId
    ){
        String response = userService.uploadVerificationImage(registrationId,file,nationalId);
        return ResponseEntity.ok(response);
    }


}
