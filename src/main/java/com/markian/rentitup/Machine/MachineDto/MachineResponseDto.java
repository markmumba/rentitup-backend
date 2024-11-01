package com.markian.rentitup.Machine.MachineDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.markian.rentitup.Machine.MachineAvailability;
import com.markian.rentitup.Machine.MachineCondition;
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

    private MachineAvailability status;

    private MachineCondition condition;

    private String specification;

    private Boolean isAvailable;

    //private Long ownerId;

    private Long categoryId;

    //private List<BookingDto> bookings;

    //private List<MaintenanceRecordDto> maintenanceRecords;

    //private List<MachineImageDto> machineImages;


}
