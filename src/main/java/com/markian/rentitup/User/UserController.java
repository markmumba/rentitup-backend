package com.markian.rentitup.User;

import com.markian.rentitup.User.UserDto.UserListResponseDto;
import com.markian.rentitup.User.UserDto.UserRequestDto;
import com.markian.rentitup.User.UserDto.UserResponseDto;
import com.markian.rentitup.User.UserDto.VerifyCollectorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserListResponseDto>> getAllUsers() {
        List<UserListResponseDto> userListResponseDtoList = userService.getAllUsers();
        return ResponseEntity.ok(userListResponseDtoList);
    }

    @GetMapping("/user-profile")
    public ResponseEntity<UserResponseDto> getLoggedInUserProfile(@AuthenticationPrincipal User user) {
        UserResponseDto userResponseDto = userService.getLoggedInUserInfo(user.getId());
        return ResponseEntity.ok(userResponseDto);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") Long id) {
        UserResponseDto userResponseDto = userService.getUserById(id);
        return ResponseEntity.ok(userResponseDto);
    }


    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> updateUser(
            @PathVariable("id") Long id,
            @ModelAttribute UserRequestDto userRequestDto
    ) {
        Map<String, String> response = userService.updateUser(id, userRequestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        String response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/unverified-users")
    public ResponseEntity<List<UserListResponseDto>> getUnverifiedUsers(
    ) {
        List<UserListResponseDto> users = userService.getUnverifiedCollectors();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/verify-collector")
    public ResponseEntity<String> verifyCollector(@RequestBody VerifyCollectorRequest verifyCollectorRequest,
    @AuthenticationPrincipal User user) {
        Long id = user.getId();
        CompletableFuture.runAsync(() -> userService.verifyCollector(verifyCollectorRequest, id));
        return ResponseEntity.ok("Request received. Processing in background.");
    }

}
