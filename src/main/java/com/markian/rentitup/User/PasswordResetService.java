package com.markian.rentitup.User;


import com.markian.rentitup.Exceptions.UserException;

public interface PasswordResetService {
    String initiatePasswordReset(String email) throws UserException;
    String confirmPasswordReset(String token,String newPassword);
}
