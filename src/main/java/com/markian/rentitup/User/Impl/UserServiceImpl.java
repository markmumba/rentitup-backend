package com.markian.rentitup.User.Impl;

import com.markian.rentitup.Exceptions.UserException;
import com.markian.rentitup.User.Role;
import com.markian.rentitup.User.User;
import com.markian.rentitup.User.UserDto.UserListResponseDto;
import com.markian.rentitup.User.UserDto.UserMapper;
import com.markian.rentitup.User.UserDto.UserRequestDto;
import com.markian.rentitup.User.UserDto.UserResponseDto;
import com.markian.rentitup.User.UserRepository;
import com.markian.rentitup.User.UserService;
import org.springframework.stereotype.Service;

import java.util.List;


//TODO how to update password

@Service
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
        try {
            List<User> users = userRepository.findAllByRole(Role.OWNER);
            return users.stream()
                    .map(userMapper::toListResponseDto)
                    .toList();

        } catch (Exception e) {
            throw new UserException("Error getting all owners" + e.getMessage(), e);
        }

    }

    @Override
    public UserResponseDto registerUser(UserRequestDto userRequestDto) throws UserException {
        try {
            if (userRepository.existsByEmail(userRequestDto.getEmail())) {
                throw new UserException("The category already exists");
            }

            User user = userRepository.save(
                    userMapper.toEntity(userRequestDto)
            );
            return userMapper.toResponseDto(user);
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserResponseDto getOwnerInfo(Long id) throws UserException {
        return null;
    }

    @Override
    public UserResponseDto getUserById(Long id) throws UserException {
        try {
            User user = userRepository.findById(id).orElseThrow(
                    () -> new UserException("No user with the id " + id)
            );
            return userMapper.toResponseDto(user);
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throw new UserException("Unable to get user information " + e.getMessage(), e);
        }
    }

    @Override
    public String updateUser(Long id, UserRequestDto userRequestDto) {
        try {
            User user = userRepository.findById(id).orElseThrow(
                    () -> new UserException("No user with the id " + id)
            );

            if (userRequestDto.getEmail() != null) {
                user.setEmail(userRequestDto.getEmail());
            }
            if (userRequestDto.getFullName() != null) {
                user.setFullName(userRequestDto.getFullName());
            }
            if (userRequestDto.getPhone() != null) {
                user.setPhone(userRequestDto.getPhone());
            }

            userRepository.save(user);
            return "User updated successfully";

        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throw new UserException("Error occurred while updating user", e);
        }
    }

    @Override
    public String deleteUser(Long id) throws UserException {
        try{
            User user = userRepository.findById(id).orElseThrow(
                    () -> new UserException("No user with the id " + id)
            );
            userRepository.delete(user);

            return "User deleted successfully";
        } catch (UserException e) {
           throw e;
        }
        catch (Exception e) {
            throw new UserException("Error occurred when deleting user" + e.getMessage(),e);
        }
    }
}
