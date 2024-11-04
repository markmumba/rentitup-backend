package com.markian.rentitup.Booking.BookingDto;

import com.markian.rentitup.Booking.Booking;
import org.springframework.stereotype.Service;

@Service
public class BookingMapper {

    public BookingResponseDto toResponseDto(Booking booking) {
        BookingResponseDto responseDto = new BookingResponseDto();
        responseDto.setId(booking.getId());
        responseDto.setBookingCode(booking.getBookingCode());
        responseDto.setStartDate(booking.getStartDate());
        responseDto.setEndDate(booking.getEndDate());
        responseDto.setPickUpLocation(booking.getPickUpLocation());
        responseDto.setStatus(booking.getStatus());
        responseDto.setTotalAmount(booking.getTotalAmount());
        responseDto.setMachineId(booking.getMachine() != null ? booking.getMachine().getId() : null);
        responseDto.setCustomerId(booking.getCustomer() != null ? booking.getCustomer().getId() : null);

        return responseDto;
    }

    public BookingListResponseDto toListDto(Booking booking) {
        BookingListResponseDto listDto = new BookingListResponseDto();
        listDto.setId(booking.getId());
        listDto.setBookingCode(booking.getBookingCode());
        listDto.setStartDate(booking.getStartDate());
        listDto.setEndDate(booking.getEndDate());
        listDto.setStatus(booking.getStatus());
        listDto.setTotalAmount(booking.getTotalAmount());

        return listDto;
    }

    public Booking toEntity(BookingRequestDto requestDto) {
        Booking booking = new Booking();
        booking.setStartDate(requestDto.getStartDate());
        booking.setEndDate(requestDto.getEndDate());
        booking.setPickUpLocation(requestDto.getPickUpLocation());
        booking.setTotalAmount(requestDto.getTotalAmount());

        return booking;
    }
}

