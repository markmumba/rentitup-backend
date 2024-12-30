package com.markian.rentitup.MaintenanceRecord;

import com.markian.rentitup.MaintenanceRecord.MaintenanceRecordDto.MaintenanceRecordRequest;
import com.markian.rentitup.MaintenanceRecord.MaintenanceRecordDto.MaintenanceRecordResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")  // Changed base path
@Slf4j
public class MaintenanceRecordController {
    private final MaintenanceRecordService maintenanceRecordService;

    public MaintenanceRecordController(MaintenanceRecordService maintenanceRecordService) {
        this.maintenanceRecordService = maintenanceRecordService;
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping(value = "/machines/{machineId}/maintenance-records/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MaintenanceRecordResponse> createMaintenanceRecordJson(
            @PathVariable Long machineId,
            @RequestBody MaintenanceRecordRequest request
    ) {
        MaintenanceRecordResponse response = maintenanceRecordService.addRecordMetadata(machineId, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping(value = "/maintenance-records/{id}/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        maintenanceRecordService.saveRecordImage(id, file);
        return ResponseEntity.ok("File uploaded successfully");
    }




    @GetMapping("/machines/{machineId}/maintenance-records")
    public ResponseEntity<List<MaintenanceRecordResponse>> getMachineMaintenanceRecords(
            @PathVariable Long machineId
    ) {
        List<MaintenanceRecordResponse> responseList = maintenanceRecordService.getRecordsForMachine(machineId);
        return ResponseEntity.ok(responseList);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("maintenance-records")
    public ResponseEntity<List<MaintenanceRecordResponse>> getUncheckedMaintenanceRecords() {
        List<MaintenanceRecordResponse> responseList = maintenanceRecordService.getUncheckedRecords();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/maintenance-records/{id}")
    public ResponseEntity<MaintenanceRecordResponse> getMaintenanceRecord(
            @PathVariable Long id
    ) {
        MaintenanceRecordResponse response = maintenanceRecordService.getRecordById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/maintenance-records/{id}")
    public ResponseEntity<String> updateMaintenanceRecord(
            @PathVariable Long id,
            @RequestPart("request") MaintenanceRecordRequest request
    ) {
        String response= maintenanceRecordService.updateRecord(id, request);
        return ResponseEntity.ok(response);
    }



    @DeleteMapping("/maintenance-records/{id}")
    public ResponseEntity<String> deleteMaintenanceRecord(
            @PathVariable Long id
    ) {
        String response = maintenanceRecordService.deleteRecord(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("maintenance-records/{id}/verify")
    public ResponseEntity<MaintenanceRecordResponse> verifyRecord(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceRecordService.verifyMaintenanceRecord(id));
    }
}