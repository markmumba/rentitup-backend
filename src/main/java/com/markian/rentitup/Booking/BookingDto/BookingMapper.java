package com.markian.rentitup.Booking.BookingDto;

import com.markian.rentitup.Booking.Booking;
import com.markian.rentitup.MachineImage.MachineImage;
import com.markian.rentitup.User.Role;
import com.markian.rentitup.User.User;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
public class BookingMapper {


    @Data
    public static class MachineSimpleResponseDto {
        private Long id;
        private String name;
        private String imageUrl;
        private String specification;
        private UserSimpleResponseDto owner;
        // other fields as needed
    }

    @Data
    public static class UserSimpleResponseDto {
        private Long id;
        private String name;
        private String email;
        private Role role;
    }

    public BookingResponseDto toResponseDto(Booking booking) {
        BookingResponseDto responseDto = new BookingResponseDto();
        responseDto.setId(booking.getId());
        responseDto.setBookingCode(booking.getBookingCode());
        responseDto.setStartDate(booking.getStartDate());
        responseDto.setEndDate(booking.getEndDate());
        responseDto.setPickUpLocation(booking.getPickUpLocation());
        responseDto.setStatus(booking.getStatus());
        responseDto.setTotalAmount(booking.getTotalAmount());

        if (booking.getMachine() != null) {
            MachineSimpleResponseDto machineDto = new MachineSimpleResponseDto();
            machineDto.setId(booking.getMachine().getId());
            machineDto.setName(booking.getMachine().getName());
            machineDto.setSpecification(booking.getMachine().getSpecification());
            machineDto.setImageUrl(
                    booking.getMachine().getMachineImages()
                            .stream()
                            .filter(MachineImage::getIsPrimary)
                            .map(MachineImage::getUrl)
                            .findFirst()
                            .orElse(null)
            )       ;


            UserSimpleResponseDto ownerDto = new UserSimpleResponseDto();
            ownerDto.setId(booking.getMachine().getOwner().getId());
            ownerDto.setName(booking.getMachine().getOwner().getFullName());
            ownerDto.setEmail(booking.getMachine().getOwner().getEmail());
            ownerDto.setRole(booking.getMachine().getOwner().getRole());

            machineDto.setOwner(ownerDto);
            responseDto.setMachine(machineDto);


        }

        if (booking.getCustomer() != null) {
            UserSimpleResponseDto customerDto = new UserSimpleResponseDto();
            customerDto.setId(booking.getCustomer().getId());
            customerDto.setName(booking.getCustomer().getFullName());
            customerDto.setEmail(booking.getCustomer().getEmail());
            customerDto.setRole(booking.getCustomer().getRole());
            responseDto.setCustomer(customerDto);
        }

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

