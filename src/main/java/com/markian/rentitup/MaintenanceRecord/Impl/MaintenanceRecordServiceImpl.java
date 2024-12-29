package com.markian.rentitup.MaintenanceRecord.Impl;

import com.amazonaws.services.kms.model.AWSKMSException;
import com.markian.rentitup.Exceptions.MachineException;
import com.markian.rentitup.Exceptions.MachineImageException;
import com.markian.rentitup.Exceptions.MaintenanceRecordException;
import com.markian.rentitup.Machine.Machine;
import com.markian.rentitup.Machine.MachineRepository;
import com.markian.rentitup.MaintenanceRecord.MaintenanceRecord;
import com.markian.rentitup.MaintenanceRecord.MaintenanceRecordDto.MaintenanceRecordMapper;
import com.markian.rentitup.MaintenanceRecord.MaintenanceRecordDto.MaintenanceRecordRequest;
import com.markian.rentitup.MaintenanceRecord.MaintenanceRecordDto.MaintenanceRecordResponse;
import com.markian.rentitup.MaintenanceRecord.MaintenanceRecordRepository;
import com.markian.rentitup.MaintenanceRecord.MaintenanceRecordService;
import com.markian.rentitup.Utils.AwsS3Service;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class MaintenanceRecordServiceImpl implements MaintenanceRecordService {
    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private final MaintenanceRecordMapper maintenanceRecordMapper;
    private final AwsS3Service awsS3Service;
    private final MachineRepository machineRepository;

    public MaintenanceRecordServiceImpl(MaintenanceRecordRepository maintenanceRecordRepository, MaintenanceRecordMapper maintenanceRecordMapper, AwsS3Service awsS3Service, MachineRepository machineRepository) {
        this.maintenanceRecordRepository = maintenanceRecordRepository;
        this.maintenanceRecordMapper = maintenanceRecordMapper;
        this.awsS3Service = awsS3Service;
        this.machineRepository = machineRepository;
    }

    @Override
    public MaintenanceRecordResponse addRecord(Long machineId, MaintenanceRecordRequest request, MultipartFile imageRecord) {
        try {
            MaintenanceRecord maintenanceRecord = maintenanceRecordMapper.toEntity(request);

            Machine machine = findMachine(machineId);
            maintenanceRecord.setMachine(machine);

            if (imageRecord != null && !imageRecord.isEmpty()) {
                String imageUrl = awsS3Service.saveImageToS3(imageRecord, "maintenanceRecord/");
                maintenanceRecord.setImageRecordUrl(imageUrl);
            }
            MaintenanceRecord savedRecord = maintenanceRecordRepository.save(maintenanceRecord);

            return maintenanceRecordMapper.toResponse(savedRecord);
        } catch (MachineException e) {
            throw e;
        } catch (Exception e) {
            throw new MaintenanceRecordException("Error while trying to add record " + e.getMessage(), e);
        }
    }

    @Override
    public MaintenanceRecordResponse getRecordById(Long id) {
        try {
            return maintenanceRecordMapper.toResponse(
                    maintenanceRecordRepository.findById(id).orElseThrow(
                            () -> new MaintenanceRecordException("Record of id " + id + "not found")
                    )
            );
        } catch (Exception e) {
            throw new MaintenanceRecordException("Error while getting a single record " + e.getMessage(), e);
        }
    }

    @Override
    public List<MaintenanceRecordResponse> getRecordsForMachine(Long machineId) {
        try {
            Machine machine = findMachine(machineId);
            List<MaintenanceRecord> recordList =
                    maintenanceRecordRepository.findAllByMachine_IdOrderByServiceDate(machineId);

            return recordList.stream()
                    .map(maintenanceRecordMapper::toResponse)
                    .toList();
        } catch (Exception e) {
            throw new MaintenanceRecordException("Error while getting maintenance records for the machine " + e.getMessage(), e);
        }
    }

    @Override
    public String updateRecord(Long id, MaintenanceRecordRequest request) {

        try {
            MaintenanceRecord existingRecord = maintenanceRecordRepository.findById(id)
                    .orElseThrow(() -> new MaintenanceRecordException("Maintenance record not found with ID: " + id));

            existingRecord.setServiceDate(request.getServiceDate());
            existingRecord.setDescription(request.getDescription());
            existingRecord.setPerformedBy(request.getPerformedBy());
            existingRecord.setNextService(request.getNextService());

            maintenanceRecordRepository.save(existingRecord);

            return "Maintenance record updated successfully";

        } catch (MaintenanceRecordException | MachineException e) {
            throw e;
        } catch (Exception e) {
            throw new MaintenanceRecordException("Failed to update maintenance record: " + e.getMessage(), e);
        }
    }

    @Override
    public String deleteRecord(Long id) {

        try {
            if (!maintenanceRecordRepository.existsById(id)) {
                throw new MaintenanceRecordException("Maintenance record not found with ID: " + id);
            }
            MaintenanceRecord record = maintenanceRecordRepository.findById(id).get();
            String imageUrl = record.getImageRecordUrl();
            maintenanceRecordRepository.deleteById(id);
            if (StringUtils.isNotBlank(imageUrl)) {
                try {
                    awsS3Service.deleteImageFromS3(imageUrl);
                } catch (Exception e) {
                    throw new MachineImageException("Unable to delete image " + e.getMessage(), e);
                }
            }
            return "Maintenance record deleted successfully";
        } catch (MaintenanceRecordException e) {
            throw e;
        } catch (Exception e) {
            throw new MaintenanceRecordException("Failed to delete maintenance record: " + e.getMessage(), e);
        }
    }


    private Machine findMachine(Long machineId) {
        return machineRepository.findById(machineId)
                .orElseThrow(() -> new MachineException("Machine not found with ID: " + machineId));
    }
}
