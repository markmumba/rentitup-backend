package com.markian.rentitup.User;

import com.markian.rentitup.Exceptions.UserException;
import com.markian.rentitup.User.UserDto.UserListResponseDto;
import com.markian.rentitup.User.UserDto.UserRequestDto;
import com.markian.rentitup.User.UserDto.UserResponseDto;

import java.util.List;

public interface UserService {
    List<UserListResponseDto> getAllUsers();

    List<UserListResponseDto> getAllOwners();

    UserResponseDto registerUser(UserRequestDto userRequestDto) throws UserException;

    UserResponseDto getOwnerInfo(Long id) throws UserException;

    UserResponseDto getUserById(Long id) throws UserException;

    String updateUser(Long id, UserRequestDto userRequestDto) throws UserException;

    String deleteUser(Long id) throws UserException;

}
