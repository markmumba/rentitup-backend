package com.markian.rentitup.MaintenanceRecord;

import com.markian.rentitup.MaintenanceRecord.MaintenanceRecordDto.MaintenanceRecordRequest;
import com.markian.rentitup.MaintenanceRecord.MaintenanceRecordDto.MaintenanceRecordResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MaintenanceRecordService {
    MaintenanceRecordResponse addRecord (Long machineId, MaintenanceRecordRequest request, MultipartFile imageRecord);
    MaintenanceRecordResponse getRecordById (Long id);
    List<MaintenanceRecordResponse> getRecordsForMachine(Long machineId);
    String updateRecord(Long id,MaintenanceRecordRequest request);
    String deleteRecord(Long id);

}
