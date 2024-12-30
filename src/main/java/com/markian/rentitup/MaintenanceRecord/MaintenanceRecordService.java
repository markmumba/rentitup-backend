package com.markian.rentitup.MaintenanceRecord;

import com.markian.rentitup.MaintenanceRecord.MaintenanceRecordDto.MaintenanceRecordRequest;
import com.markian.rentitup.MaintenanceRecord.MaintenanceRecordDto.MaintenanceRecordResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MaintenanceRecordService {
    MaintenanceRecordResponse addRecordMetadata(Long machineId, MaintenanceRecordRequest request);
    String saveRecordImage(Long machineId, MultipartFile imageRecord);
    MaintenanceRecordResponse getRecordById (Long id);
    List<MaintenanceRecordResponse> getRecordsForMachine(Long machineId);
    List<MaintenanceRecordResponse> getUncheckedRecords();
    String updateRecord(Long id,MaintenanceRecordRequest request);
    String deleteRecord(Long id);
    MaintenanceRecordResponse verifyMaintenanceRecord(Long recordId);

}
