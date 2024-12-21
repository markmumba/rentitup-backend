package com.markian.rentitup.User;


import com.markian.rentitup.Exceptions.UserException;
import com.markian.rentitup.User.UserDto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserResponseDto registerUser(UserRequestDto userRequestDto) throws UserException;

    AuthResponse login(LoginRequest loginRequest) throws UserException;

    List<UserListResponseDto> getAllUsers();

    UserResponseDto getLoggedInUserInfo(Long id) throws UserException;


    User findOrCreateOAuth2User(String email, String name);

    UserResponseDto getUserById(Long id) throws UserException;

    String verifyCollector(VerifyCollectorRequest verifyCollectorRequest, Long adminId)throws  UserException;

    Map<String, String> updateUser(Long id, UserRequestDto userRequestDto);
    String deleteUser(Long id) throws UserException;

    String uploadVerificationImage(String registrationId,MultipartFile photo,String nationalId) throws UserException;

    List<UserListResponseDto> getUnverifiedCollectors();

}
