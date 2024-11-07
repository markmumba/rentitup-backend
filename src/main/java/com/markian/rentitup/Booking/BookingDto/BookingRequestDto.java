package com.markian.rentitup.Booking.BookingDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookingRequestDto {

    @NotBlank(message = "start date should not be null")
    private LocalDate startDate;

    @NotBlank(message = "end date should not be null")
    private LocalDate endDate;

    @NotBlank(message = "pick up Location should not be null")
    private String pickUpLocation;

    @NotBlank(message = "pick up Location should not be null")
    private BigDecimal totalAmount;

    @NotNull
    private Long machineId;

    @NotNull
    private Long customerId;

}
