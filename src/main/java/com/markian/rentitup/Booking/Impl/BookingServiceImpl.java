package com.markian.rentitup.Booking.Impl;

import com.markian.rentitup.Booking.Booking;
import com.markian.rentitup.Booking.BookingDto.BookingListResponseDto;
import com.markian.rentitup.Booking.BookingDto.BookingMapper;
import com.markian.rentitup.Booking.BookingDto.BookingRequestDto;
import com.markian.rentitup.Booking.BookingDto.BookingResponseDto;
import com.markian.rentitup.Booking.BookingRepository;
import com.markian.rentitup.Booking.BookingService;
import com.markian.rentitup.Booking.BookingStatus;
import com.markian.rentitup.Exceptions.BookingException;
import com.markian.rentitup.Exceptions.MachineException;
import com.markian.rentitup.Machine.Machine;
import com.markian.rentitup.Machine.MachineRepository;
import com.markian.rentitup.User.User;
import com.markian.rentitup.User.UserRepository;
import com.markian.rentitup.Utils.Utils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final MachineRepository machineRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    public BookingServiceImpl(BookingRepository bookingRepository, MachineRepository machineRepository, UserRepository userRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto) throws BookingException {
        try {
            Machine machine = machineRepository.findById(bookingRequestDto.getMachineId())
                    .orElseThrow(() -> new MachineException("Machine with id " + bookingRequestDto.getMachineId() + " not found"));
            List<Booking> existingBookings = bookingRepository.findAllByMachineId(bookingRequestDto.getMachineId());

            LocalDate requestedStartDate = bookingRequestDto.getStartDate();
            LocalDate requestedEndDate = bookingRequestDto.getEndDate();

            for (Booking booking : existingBookings) {
                LocalDate existingStartDate = booking.getStartDate();
                LocalDate existingEndDate = booking.getEndDate();

                boolean isOverlapping =
                        (requestedStartDate.isBefore(existingEndDate) && requestedEndDate.isAfter(existingStartDate) ||
                                requestedStartDate.equals(existingStartDate) || requestedEndDate.equals(existingEndDate));

                if (isOverlapping) {
                    throw new BookingException("Cannot book machine during this period");
                }
            }

            Booking booking = new Booking();
            String bookingCode = Utils.generateConfirmationCode(10);
            booking.setBookingCode(bookingCode);
            booking.setStartDate(requestedStartDate);
            booking.setEndDate(requestedEndDate);
            booking.setPickUpLocation(bookingRequestDto.getPickUpLocation());
            booking.setStatus(BookingStatus.PENDING);
            booking.setTotalAmount(bookingRequestDto.getTotalAmount());

            booking.setMachine(machine);
            User customer = userRepository.findById(bookingRequestDto.getCustomerId())
                    .orElseThrow(() -> new BookingException("Customer with ID " + bookingRequestDto.getCustomerId() + " not found"));
            booking.setCustomer(customer);

            booking = bookingRepository.save(booking);
            return bookingMapper.toResponseDto(booking);
        } catch (Exception e) {
            throw new BookingException("Unable to book " + e.getMessage(), e);
        }
    }

    @Override
    public List<BookingListResponseDto> getAllBookings() {
        try {

            return bookingRepository.findAll()
                    .stream()
                    .map(bookingMapper::toListDto)
                    .toList();
        } catch (Exception e) {
            throw new BookingException("Error getting bookings " + e.getMessage(), e);
        }
    }

    @Override
    public List<BookingListResponseDto> getBookingsByUser(Long userId) throws BookingException {
        try {
            List<Booking> bookings = bookingRepository.findAllByCustomerId(userId);
            return bookings.stream()
                    .map(bookingMapper::toListDto)
                    .toList();
        } catch (Exception e) {
            throw new BookingException("Error getting users bookings " + e.getMessage(), e);
        }

    }

    @Override
    public BookingResponseDto getBooking(Long id) throws BookingException {
        try {
            Booking booking = bookingRepository.findById(id).orElseThrow(
                    () -> new BookingException("Booking of id " + id + "not found")
            );
            return bookingMapper.toResponseDto(booking);
        } catch (BookingException e) {
            throw e;
        } catch (Exception e) {
            throw new BookingException("Error getting booking details " + e.getMessage(), e);
        }
    }

    @Override
    public List<BookingListResponseDto> getBookingsByStatus(Long userId, String status) {
        try {
            BookingStatus bookingStatus;
            try {
                bookingStatus = BookingStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                throw new BookingException("Invalid Status", e);
            }
            return bookingRepository.findAllByCustomerIdAndStatus(userId, bookingStatus)
                    .stream()
                    .map(bookingMapper::toListDto)
                    .toList();

        } catch (Exception e) {
            throw new BookingException("Error getting bookings by status " + e.getMessage(), e);
        }
    }

    @Override
    public String updateBooking(Long id, BookingRequestDto bookingRequestDto) throws BookingException {
        try {
            Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new BookingException("Booking with id " + id + " not found"));

            if (bookingRequestDto.getStartDate() != null) {
                booking.setStartDate(bookingRequestDto.getStartDate());
            }

            if (bookingRequestDto.getEndDate() != null) {
                booking.setEndDate(bookingRequestDto.getEndDate());
            }

            if (bookingRequestDto.getPickUpLocation() != null) {
                booking.setPickUpLocation(bookingRequestDto.getPickUpLocation());
            }
            bookingRepository.save(booking);

            return "Booking updated successfully";

        } catch (BookingException e) {
            throw e;
        } catch (Exception e) {
            throw new BookingException("Unable to update booking: " + e.getMessage(), e);
        }
    }

    @Override
    public String deleteBooking(Long id) throws BookingException {
        try {
            Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new BookingException("Booking with id " + id + " not found"));

            bookingRepository.delete(booking);
            return "Booking deleted successfully";
        } catch (Exception e) {
            throw new BookingException("Unable to delete booking " + e.getMessage(), e);
        }
    }

    @Override
    public String updateStatus(Long id, String status) throws BookingException {
        try {
            BookingStatus bookingStatus;
            try {
                bookingStatus = BookingStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                throw new BookingException("Invalid Status", e);
            }

            Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new BookingException("Booking with id " + id + " not found"));

            booking.setStatus(bookingStatus);
            bookingRepository.save(booking);
            return "Status updated successfully";

        } catch (Exception e) {
            throw new BookingException("Unable to update Status " + e.getMessage(), e);
        }
    }

    @Override
    public BookingResponseDto getBookingByCode(String code) throws BookingException {
        try {
            Booking booking = bookingRepository.findByBookingCode(code)
                    .orElseThrow(() -> new BookingException("Booking with code " + code + " not found"));

            return bookingMapper.toResponseDto(booking);
        } catch (Exception e) {
            throw new BookingException("Unable to find booking with code " + e.getMessage(), e);
        }

    }
}
