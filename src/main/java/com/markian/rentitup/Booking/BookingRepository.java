package com.markian.rentitup.Booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByCustomerIdAndStatus(Long userId, BookingStatus bookingStatus);

    List<Booking> findAllByCustomerId(Long userId);


    List<Booking> findAllByMachineId(Long machineId);

    Optional<Booking> findByBookingCode(String bookingCode);

    List<Booking> findAllByMachineOwnerId(Long ownerId);
}
