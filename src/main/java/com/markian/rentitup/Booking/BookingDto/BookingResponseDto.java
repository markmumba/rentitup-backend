package com.markian.rentitup.Booking.BookingDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.markian.rentitup.Booking.Booking;
import com.markian.rentitup.Booking.BookingStatus;
import com.markian.rentitup.Machine.Machine;
import com.markian.rentitup.User.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingResponseDto {

    private String bookingCode;

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private String pickUpLocation;

    private BookingStatus status;

    private BigDecimal totalAmount;

    private Long machineId;

    private Long customerId;
}