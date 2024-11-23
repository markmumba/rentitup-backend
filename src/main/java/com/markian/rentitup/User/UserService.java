package com.markian.rentitup.User;

import com.markian.rentitup.Exceptions.UserException;
import com.markian.rentitup.User.UserDto.*;

import java.util.List;

public interface UserService {
    UserResponseDto registerUser(UserRequestDto userRequestDto) throws UserException;

    AuthResponse login (LoginRequest loginRequest)throws UserException;

    List<UserListResponseDto> getAllUsers();

    List<UserListResponseDto> getAllOwners();

    UserResponseDto getLoggedInUserInfo(String email) throws UserException;

    UserResponseDto getUserById(Long id) throws UserException;

    String updateUser(Long id, UserRequestDto userRequestDto) throws UserException;

    String deleteUser(Long id) throws UserException;

    boolean verifyUser(Long id) throws UserException;

}
