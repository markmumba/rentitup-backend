package com.markian.rentitup.Review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review ,Long> {
    List<Review> findAllByBooking_MachineId (Long machineId);
}
