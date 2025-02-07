package com.markian.rentitup.Booking;

import com.markian.rentitup.Booking.BookingDto.BookingListResponseDto;
import com.markian.rentitup.Booking.BookingDto.BookingRequestDto;
import com.markian.rentitup.Booking.BookingDto.BookingResponseDto;
import com.markian.rentitup.Exceptions.BookingException;

import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(BookingRequestDto bookingRequestDto) throws BookingException;

    List<BookingListResponseDto> getAllBookings();

    List<BookingListResponseDto> getBookingsByUser(Long userId) throws BookingException;

    List<BookingListResponseDto> getBookingsForOwner(Long ownerId) throws BookingException;

    BookingResponseDto getBooking(Long id) throws BookingException;

    List<BookingListResponseDto> getBookingsByStatus(Long userId, String status);

    List<BookingListResponseDto> getBookingsByMachine(Long machineId);

    String updateBooking(Long id, BookingRequestDto bookingRequestDto) throws BookingException;

    String deleteBooking(Long id) throws BookingException;

    String updateStatus(Long id, String status) throws BookingException;

    BookingResponseDto getBookingByCode(String code) throws BookingException;

    List<String> getBookingStatus() ;


}
