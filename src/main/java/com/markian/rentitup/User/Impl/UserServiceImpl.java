package com.markian.rentitup.User.Impl;

import com.markian.rentitup.Exceptions.UserException;
import com.markian.rentitup.User.Role;
import com.markian.rentitup.User.User;
import com.markian.rentitup.User.UserDto.*;
import com.markian.rentitup.User.UserRepository;
import com.markian.rentitup.User.UserService;
import com.markian.rentitup.Utils.AwsS3Service;
import com.markian.rentitup.Utils.JwtTokenUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


//TODO how to update password
//TODO Remove the private send email and use the one in utils folder

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final AwsS3Service awsS3Service;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, JavaMailSender mailSender, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager, AwsS3Service awsS3Service) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.awsS3Service = awsS3Service;
    }

    @Override
    @Transactional
    public UserResponseDto registerUser(UserRequestDto userRequestDto) throws UserException {
        try {
            validateUserRegistration(userRequestDto);

            userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

            User user = userMapper.toEntity(userRequestDto);

            collectorsRegistrationSettings(user);

            user.setCreatedAt(LocalDateTime.now());
            userRepository.save(user);
            return userMapper.toResponseDto(user);

        } catch (UserException e) {
            logger.error("User registration failed", e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during user registration", e);
            throw new UserException("Failed to register user: " + e.getMessage(), e);
        }
    }


    @Override
    public AuthResponse login(LoginRequest loginRequest) throws UserException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            var user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new UserException("User not found"));
            if (user.getVerified().equals(false)) {
                throw new UserException("The account is not yet verified");
            }
            var token = jwtTokenUtil.generateToken(user);

            AuthResponse authResponse = new AuthResponse();

            authResponse.setToken(token);
            authResponse.setRole(user.getRole().name());
            ;
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
    public UserResponseDto getLoggedInUserInfo(Long id) throws UserException {
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
    public User findOrCreateOAuth2User(String email, String name) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setFullName(name);
                    newUser.setRole(Role.CUSTOMER);  // Default role
                    newUser.setVerified(true);
                    newUser.setCreatedAt(LocalDateTime.now());
                    newUser.setVerifiedAt(LocalDateTime.now());
                    newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    return userRepository.save(newUser);
                });
    }

    @Override
    public UserResponseDto getUserById(Long id) throws UserException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new UserException("user not authenticated");
            }
            User currentUser = (User) authentication.getPrincipal();
            Long currentUserId = currentUser.getId();

            boolean isAdmin = currentUser.getAuthorities().stream().anyMatch(
                    grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));

            if (id.equals(currentUserId) || isAdmin) {
                User user = userRepository.findById(id)
                        .orElseThrow(() -> new UserException("No user with the id " + id));

                return userMapper.toResponseDto(user);
            } else {
                throw new UserException("Access denied: unauthorized to access this user data");
            }
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throw new UserException("Unable to get user information " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public String verifyCollector(VerifyCollectorRequest verifyCollectorRequest, Long adminId) throws UserException {
        User user = findUserById(verifyCollectorRequest.getId());

        return verifyCollectorRequest.isStatus()
                ? processVerifiedCollector(user, adminId)
                : processRejectedCollector(user);
    }


    public Map<String, String> updateUser(Long id, UserRequestDto userRequestDto) {
        try {
            User user = userRepository.findById(id).orElseThrow(
                    () -> new UserException("No user with the id " + id)
            );

            // Only update email if a non-empty email is provided
            if (userRequestDto.getEmail() != null && !userRequestDto.getEmail().trim().isEmpty()) {
                user.setEmail(userRequestDto.getEmail());
            }

            if (userRequestDto.getFullName() != null && !userRequestDto.getFullName().trim().isEmpty()) {
                user.setFullName(userRequestDto.getFullName());
            }

            if (userRequestDto.getPhone() != null && !userRequestDto.getPhone().trim().isEmpty()) {
                user.setPhone(userRequestDto.getPhone());
            }

            if (userRequestDto.getProfileImage() != null && !userRequestDto.getProfileImage().isEmpty()) {
                String profileImageUrl = awsS3Service.saveImageToS3(
                        userRequestDto.getProfileImage(),
                        "profile/"
                );
                user.setProfileImageUrl(profileImageUrl);
            }

            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            Map<String, String> response = new HashMap<>();
            response.put("message", "User updated successfully");
            response.put("profileImageUrl", user.getProfileImageUrl());

            return response;

        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throw new UserException("Error occurred while updating user" + e.getMessage(), e);
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
    @Transactional
    public String uploadVerificationImage(String registrationId, MultipartFile photo,String nationalId) throws UserException {
        try {
            if (photo.isEmpty()) {
                throw new UserException("No image was provided");
            }
            User user = userRepository.findByRegistrationId(registrationId).orElseThrow(
                    () -> new UserException("User with the given registration id not found")

            );
            String url = awsS3Service.saveImageToS3(photo, "identification/");

            user.setNationalId(nationalId);
            user.setVerificationImageUrl(url);
            userRepository.save(user);

            return "Image Uploaded Successfully";
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throw new UserException("Unable to upload verification image " + e.getMessage(), e);
        }
    }

    @Override
    public List<UserListResponseDto> getUnverifiedCollectors() {
        try {
            List<User> users = userRepository.findByVerifiedFalse();
            return users.stream()
                    .map(userMapper::toListResponseDto)
                    .toList();
        } catch (Exception e) {
            throw new UserException("Unable to get unverified collectors: " + e.getMessage(), e);
        }
    }


    /**
     * Private functions below
     */

    private boolean isValidRole(String role) {
        for (Role r : Role.values()) {
            if (r.name().equals(role)) {
                return true;
            }
        }
        return false;
    }

    private void validateUserRegistration(UserRequestDto userRequestDto) {

        validateRole(userRequestDto.getRole());
        checkEmailUniqueness(userRequestDto.getEmail());

    }

    private void validateRole(String role) {
        if (role == null || role.isBlank()) {
            throw new UserException("Role must be specified.");
        }
        if (!isValidRole(role)) {
            throw new UserException("Invalid role specified.");
        }
    }

    private void checkEmailUniqueness(String email) throws UserException {
        if (userRepository.existsByEmail(email)) {
            throw new UserException(email + " already exists.");
        }

    }

    private void collectorsRegistrationSettings(User user) {
        if (Role.COLLECTOR.equals(user.getRole())) {
            String registrationId = UUID.randomUUID().toString();
            user.setVerified(false);
            user.setRegistrationId(registrationId);
        } else {
            user.setVerified(true);
            user.setRegistrationId(null);
        }
    }

    private void sendVerificationEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);

    }

    private User findUserById(Long id) throws UserException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException("No user with the id " + id));
    }

    private String processVerifiedCollector(User user, Long adminId) {
        userRepository.verifyUser(true, LocalDateTime.now(), adminId, null, user.getId());

        CompletableFuture.runAsync(() ->
                sendVerificationEmail(
                        user.getEmail(),
                        "Successfully Account Verification",
                        "Your account has been successfully verified. You can now log in and view available jobs."
                )
        );

        return "Collector verified successfully and email sent.";
    }

    private String processRejectedCollector(User user) {
        CompletableFuture.runAsync(() ->
                sendVerificationEmail(
                        user.getEmail(),
                        "Account creation denied",
                        "Your account was not accepted. Please try registering with more details."
                )
        );

        userRepository.delete(user);
        return "Collector account not accepted and was removed.";
    }


}
