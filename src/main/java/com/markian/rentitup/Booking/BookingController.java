package com.markian.rentitup.Booking;

import com.markian.rentitup.Booking.BookingDto.BookingListResponseDto;
import com.markian.rentitup.Booking.BookingDto.BookingRequestDto;
import com.markian.rentitup.Booking.BookingDto.BookingResponseDto;
import com.markian.rentitup.User.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService, UserRepository userRepository) {
        this.bookingService = bookingService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BookingListResponseDto>> getAllBookings() {
        List<BookingListResponseDto> bookingList = bookingService.getAllBookings();
        return ResponseEntity.ok(bookingList);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(
            @RequestBody BookingRequestDto bookingRequestDto
    ) {
        BookingResponseDto bookingResponseDto = bookingService.createBooking(bookingRequestDto);
        return ResponseEntity.ok(bookingResponseDto);
    }


    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingListResponseDto>> getBookingsByUser(
            @PathVariable("userId") Long userId
    ) {
        List<BookingListResponseDto> bookingList = bookingService.getBookingsByUser(userId);
        return ResponseEntity.ok(bookingList);
    }

    @PreAuthorize("hasAuthority('OWNER')")
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<BookingListResponseDto>> getBookingForOwner(
            @PathVariable("ownerId") Long ownerId
    ) {
        List<BookingListResponseDto> bookingList = bookingService.getBookingsForOwner(ownerId);
        return ResponseEntity.ok(bookingList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getBooking(
            @PathVariable("id") Long id
    ) {
        BookingResponseDto booking = bookingService.getBooking(id);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/user/{userId}/status")
    public ResponseEntity<List<BookingListResponseDto>> getBookingsByStatus(
            @PathVariable("userId") Long userId,
            @RequestParam(name = "status", required = true) String status
    ) {
        List<BookingListResponseDto> bookingList = bookingService.getBookingsByStatus(userId, status);
        return ResponseEntity.ok(bookingList);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateBooking(
            @PathVariable("id") Long id,
            @RequestBody BookingRequestDto bookingRequestDto
    ) {
        String response = bookingService.updateBooking(id, bookingRequestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(
            @PathVariable("id") Long id
    ) {
        String response = bookingService.deleteBooking(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('OWNER')")
    @PutMapping("/{id}/status-update")
    public ResponseEntity<String> updateStatus(
            @PathVariable("id") Long id,
            @RequestParam(name = "status") String status
    ) {
        String response = bookingService.updateStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/machine/{machineId}")
    public ResponseEntity<List<BookingListResponseDto>> getBookingsByMachine(
            @PathVariable("machineId") Long machineId
    ) {
        List<BookingListResponseDto> bookingListResponseDtos = bookingService.getBookingsByMachine(machineId);
        return ResponseEntity.ok(bookingListResponseDtos);
    }

    @GetMapping("/get-by-code")
    public ResponseEntity<BookingResponseDto> getBookingByCode(
            @RequestParam(name = "code") String bookingCode
    ) {
        BookingResponseDto booking = bookingService.getBookingByCode(bookingCode);
        return ResponseEntity.ok(booking);
    }

    @PreAuthorize("hasAuthority('OWNER')")
    @GetMapping("/booking-status-list")
    public ResponseEntity<List<String>> getBookingStatus() {

        List<String> response = bookingService.getBookingStatus();
        return ResponseEntity.ok(response);
    }
}
