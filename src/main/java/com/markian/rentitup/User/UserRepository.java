package com.markian.rentitup.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByRole(Role role);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String username);

    Optional<User> findByRegistrationId(String registrationId);
    Optional<User> findByResetToken(String token);

    @Modifying
    @Query("UPDATE user_table u SET u.verified = :verified, u.verifiedAt = :verifiedAt, u.verifiedByAdminId = :adminId,u.registrationId=:registrationId WHERE u.id = :userId")
    void verifyUser(@Param("verified") boolean verified, @Param("verifiedAt") LocalDateTime verifiedAt, @Param("adminId") Long adminId, @Param("registrationId") String registrationId, @Param("userId") Long userId);

    List<User> findByVerifiedFalse();
}
