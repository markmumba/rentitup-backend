package com.markian.rentitup.User.Impl;

import com.markian.rentitup.Exceptions.UserException;
import com.markian.rentitup.User.PasswordResetService;
import com.markian.rentitup.User.User;
import com.markian.rentitup.User.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;

    private final JavaMailSender mailSender;

    private final PasswordEncoder passwordEncoder;

    @Value("${frontend.url}")
    private String frontEndUrl;

    public PasswordResetServiceImpl(UserRepository userRepository, JavaMailSender mailSender, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }


    private void sendResetEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("Click the link to reset your password: " + frontEndUrl +"/reset-password?token=" + token);

        mailSender.send(message);
    }

    @Override
    public String initiatePasswordReset(String email) throws UserException {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(
                    () -> new UserException("User not found ")
            );

            String token = generateToken();
            user.setResetToken(token);
            user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
            userRepository.save(user);

            sendResetEmail(user.getEmail(), token);
            return "Password reset link sent to your email";
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throw new UserException("Error resetting password" + e.getMessage(), e);
        }

    }

    @Override
    public String confirmPasswordReset(String token, String newPassword) {
        try {
            User user = userRepository.findByResetToken(token)
                    .filter(u -> u.getResetTokenExpiry().isAfter(LocalDateTime.now()))
                    .orElseThrow(() -> new UserException("Unable to find user by the given token "));

            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
            userRepository.save(user);
            return "Password reset Successfully";
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throw new UserException("Error confirming reset" + e.getMessage(), e);
        }
    }
}
