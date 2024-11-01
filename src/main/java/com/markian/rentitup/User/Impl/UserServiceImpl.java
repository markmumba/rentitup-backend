package com.markian.rentitup.User.Impl;

import com.markian.rentitup.Exceptions.UserException;
import com.markian.rentitup.User.User;
import com.markian.rentitup.User.UserDto.UserListResponseDto;
import com.markian.rentitup.User.UserDto.UserMapper;
import com.markian.rentitup.User.UserDto.UserRequestDto;
import com.markian.rentitup.User.UserDto.UserResponseDto;
import com.markian.rentitup.User.UserRepository;
import com.markian.rentitup.User.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserListResponseDto> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return users.stream()
                    .map(userMapper::toListResponseDto)
                    .toList();
        } catch (Exception e) {
            throw new UserException("Error getting all users" + e.getMessage(), e);
        }
    }

    @Override
    public List<UserListResponseDto> getAllOwners() {
        try{

        }catch (Exception e) {
            throw new UserException("Error getting all owners" + e.getMessage(), e);
        }

    }

    @Override
    public UserResponseDto registerUser(UserRequestDto userRequestDto) throws UserException {
        return null;
    }

    @Override
    public UserResponseDto getOwnerInfo(Long id) throws UserException {
        return null;
    }

    @Override
    public UserResponseDto getUserById(Long id) throws UserException {
        return null;
    }

    @Override
    public String updateUser(Long id, UserRequestDto userRequestDto) throws UserException {
        return "";
    }

    @Override
    public String deleteUser(Long id) throws UserException {
        return "";
    }
}
