package com.markian.rentitup.Machine.MachineDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.markian.rentitup.Machine.MachineAvailability;
import com.markian.rentitup.Machine.MachineCondition;
import com.markian.rentitup.MachineImage.MachineImage;
import com.markian.rentitup.MachineImage.MachineImageDto.MachineImageResponseDto;
import com.markian.rentitup.User.User;
import com.markian.rentitup.User.UserDto.UserResponseDto;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MachineResponseDto {

    private Long id;

    private String name;

    private String description;

    private BigDecimal basePrice;

    private Boolean verified;

    private MachineAvailability status;

    private MachineCondition condition;

    private String specification;

    private Boolean isAvailable;

    private MachineMapperImpl.UserSimpleDto owner;

    private Long categoryId;

    //private List<BookingDto> bookings;

    //private List<MaintenanceRecordDto> maintenanceRecords;

    private List<MachineMapperImpl.MachineImageDto> machineImages;


}
