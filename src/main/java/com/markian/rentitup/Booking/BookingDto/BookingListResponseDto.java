package com.markian.rentitup.Booking.BookingDto;


import com.markian.rentitup.Booking.BookingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookingListResponseDto {

    private String bookingCode;

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private BookingStatus status;

    private BigDecimal totalAmount;
}
