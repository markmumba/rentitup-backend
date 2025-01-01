package com.markian.rentitup.MaintenanceRecord.MaintenanceRecordDto;

import com.markian.rentitup.Machine.Machine;
import com.markian.rentitup.Machine.MachineDto.MachineMapper;
import com.markian.rentitup.MaintenanceRecord.MaintenanceRecord;
import org.springframework.stereotype.Service;

@Service
public class MaintenanceRecordMapper {

    private final MachineMapper machineMapper;

    public MaintenanceRecordMapper(MachineMapper machineMapper) {
        this.machineMapper = machineMapper;
    }


    public MaintenanceRecordResponse toResponse(MaintenanceRecord entity) {
        if (entity == null) {
            return null;
        }

        MaintenanceRecordResponse response = new MaintenanceRecordResponse();
        response.setId(entity.getId());
        response.setServiceDate(entity.getServiceDate());
        response.setChecked(entity.getChecked());
        response.setDescription(entity.getDescription());
        response.setPerformedBy(entity.getPerformedBy());
        response.setNextService(entity.getNextService());
        response.setImageRecordUrl(entity.getImageRecordUrl());
        response.setMachine(machineMapper.toResponseDto(entity.getMachine()));

        return response;
    }

    public MaintenanceRecord toEntity(MaintenanceRecordRequest request) {
        if (request == null) {
            return null;
        }

        MaintenanceRecord entity = new MaintenanceRecord();
        entity.setServiceDate(request.getServiceDate());
        entity.setDescription(request.getDescription());
        entity.setPerformedBy(request.getPerformedBy());
        entity.setNextService(request.getNextService());

        return entity;
    }



}