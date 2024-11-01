package com.markian.rentitup.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User ,Long> {
    List<User> findAllByRole(Role role);

    boolean existsByEmail( String email);
}
