package com.markian.rentitup.MaintenanceRecord.MaintenanceRecordDto;

import com.markian.rentitup.Machine.MachineDto.MachineResponseDto;
import lombok.Data;

import java.time.LocalDate;

@Data

public class MaintenanceRecordResponse {
    private Long id;

    private LocalDate serviceDate;

    private String description;

    private Boolean checked;

    private String performedBy;

    private LocalDate nextService;

    private String imageRecordUrl;

    private MachineResponseDto machine;
}
