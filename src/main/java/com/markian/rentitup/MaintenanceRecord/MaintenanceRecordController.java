package com.markian.rentitup.MaintenanceRecord;

import com.markian.rentitup.MaintenanceRecord.MaintenanceRecordDto.MaintenanceRecordRequest;
import com.markian.rentitup.MaintenanceRecord.MaintenanceRecordDto.MaintenanceRecordResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")  // Changed base path
public class MaintenanceRecordController {
    private final MaintenanceRecordService maintenanceRecordService;

    public MaintenanceRecordController(MaintenanceRecordService maintenanceRecordService) {
        this.maintenanceRecordService = maintenanceRecordService;
    }

    @PostMapping(value = "/machines/{machineId}/maintenance-records", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MaintenanceRecordResponse> createMaintenanceRecord(
            @RequestPart("request") MaintenanceRecordRequest request,
            @PathVariable Long machineId,
            @RequestPart("file") MultipartFile file
    ) {
        MaintenanceRecordResponse response = maintenanceRecordService.addRecord(machineId, request, file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/machines/{machineId}/maintenance-records")
    public ResponseEntity<List<MaintenanceRecordResponse>> getMachineMaintenanceRecords(
            @PathVariable Long machineId
    ) {
        List<MaintenanceRecordResponse> responseList = maintenanceRecordService.getRecordsForMachine(machineId);
        return ResponseEntity.ok(responseList);
    }

    // These endpoints don't need machineId as they operate on the maintenance record directly
    @GetMapping("/maintenance-records/{id}")
    public ResponseEntity<MaintenanceRecordResponse> getMaintenanceRecord(
            @PathVariable Long id
    ) {
        MaintenanceRecordResponse response = maintenanceRecordService.getRecordById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/maintenance-records/{id}")
    public ResponseEntity<MaintenanceRecordResponse> updateMaintenanceRecord(
            @PathVariable Long id,
            @RequestPart("request") MaintenanceRecordRequest request
    ) {
        String result = maintenanceRecordService.updateRecord(id, request);
        // Consider updating the service to return MaintenanceRecordResponse instead of String
        MaintenanceRecordResponse response = maintenanceRecordService.getRecordById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/maintenance-records/{id}")
    public ResponseEntity<Void> deleteMaintenanceRecord(
            @PathVariable Long id
    ) {
        maintenanceRecordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}