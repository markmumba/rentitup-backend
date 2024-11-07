package com.markian.rentitup.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User ,Long> {
    List<User> findAllByRole(Role role);

    boolean existsByEmail( String email);

    Optional<User> findByEmail(String username);
}
