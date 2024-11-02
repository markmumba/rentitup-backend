package com.markian.rentitup.User;

import com.markian.rentitup.User.UserDto.UserListResponseDto;
import com.markian.rentitup.User.UserDto.UserRequestDto;
import com.markian.rentitup.User.UserDto.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<List<UserListResponseDto>> getAllUsers() {
        List<UserListResponseDto> userListResponseDtoList = userService.getAllUsers();
        return ResponseEntity.ok(userListResponseDtoList);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @RequestBody UserRequestDto userRequestDto
    ) {
        UserResponseDto userResponseDto = userService.registerUser(userRequestDto);
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/owners")
    public ResponseEntity<List<UserListResponseDto>> getAllOwners() {
        List<UserListResponseDto> userListResponseDtoList = userService.getAllOwners();
        return ResponseEntity.ok(userListResponseDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") Long id) {
        UserResponseDto userResponseDto = userService.getUserById(id);
        return ResponseEntity.ok(userResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") Long id, @RequestBody UserRequestDto userRequestDto) {
        String response = userService.updateUser(id, userRequestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        String response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }

}
