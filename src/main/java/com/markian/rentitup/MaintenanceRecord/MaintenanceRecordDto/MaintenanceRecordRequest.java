package com.markian.rentitup.MaintenanceRecord.MaintenanceRecordDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Builder
public class MaintenanceRecordRequest {

    private LocalDate serviceDate;

    private String description;

    private String performedBy;

    private LocalDate nextService;

}
