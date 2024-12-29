package com.markian.rentitup.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    @Query("SELECT c FROM category c LEFT JOIN FETCH c.machines m WHERE c.id = :id AND (m.verified = true OR m IS NULL)")
    Optional<Category> findByIdWithVerifiedMachines(@Param("id") Long id);
}
