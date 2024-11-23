package com.markian.rentitup.User.Impl;

import com.markian.rentitup.Exceptions.UserException;
import com.markian.rentitup.User.Role;
import com.markian.rentitup.User.User;
import com.markian.rentitup.User.UserDto.*;
import com.markian.rentitup.User.UserRepository;
import com.markian.rentitup.User.UserService;
import com.markian.rentitup.Utils.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


//TODO how to update password

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserResponseDto registerUser(UserRequestDto userRequestDto) throws UserException {
        try {
            if (userRequestDto.getRole() == null || userRequestDto.getRole().isBlank()) {
                throw new UserException("Role must be specified.");
            }
            if (!isValidRole(userRequestDto.getRole())) {
                throw new UserException("Invalid role specified.");
            }
            if (userRepository.existsByEmail(userRequestDto.getEmail())) {
                throw new UserException(userRequestDto.getEmail() + " already exists.");
            }

            userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
            User user = userRepository.save(
                    userMapper.toEntity(userRequestDto)
            );
            return userMapper.toResponseDto(user);

        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throw new UserException("Failed to register user " + e.getMessage(), e);
        }
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) throws UserException {
        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            var user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new UserException("User not found"));

            var token = jwtTokenUtil.generateToken(user);

            AuthResponse authResponse = new AuthResponse();

            authResponse.setToken(token);
            authResponse.setRole(user.getRole().name());;
            return authResponse;
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throw new UserException("Error while logging in " + e.getMessage(), e);
        }
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

    private boolean isValidRole(String role) {
        for (Role r : Role.values()) {
            if (r.name().equals(role)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public UserResponseDto getLoggedInUserInfo(String email) throws UserException {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(
                    () -> new UserException("No user with the email " + email)
            );
            return userMapper.toResponseDto(user);
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throw new UserException("Unable to get user information " + e.getMessage(), e);
        }
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
        try {
            User user = userRepository.findById(id).orElseThrow(
                    () -> new UserException("No user with the id " + id)
            );
            userRepository.delete(user);

            return "User deleted successfully";
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throw new UserException("Error occurred when deleting user" + e.getMessage(), e);
        }
    }

    @Override
    public boolean verifyUser(Long id) throws UserException {
        return false;
    }
}
