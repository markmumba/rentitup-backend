package com.markian.rentitup.MaintenanceRecord.Impl;

import com.markian.rentitup.Exceptions.MachineException;
import com.markian.rentitup.Exceptions.MachineImageException;
import com.markian.rentitup.Exceptions.MaintenanceRecordException;
import com.markian.rentitup.Machine.Impl.MachineServiceImpl;
import com.markian.rentitup.Machine.Machine;
import com.markian.rentitup.Machine.MachineRepository;
import com.markian.rentitup.Machine.MachineService;
import com.markian.rentitup.MaintenanceRecord.MaintenanceRecord;
import com.markian.rentitup.MaintenanceRecord.MaintenanceRecordDto.MaintenanceRecordMapper;
import com.markian.rentitup.MaintenanceRecord.MaintenanceRecordDto.MaintenanceRecordRequest;
import com.markian.rentitup.MaintenanceRecord.MaintenanceRecordDto.MaintenanceRecordResponse;
import com.markian.rentitup.MaintenanceRecord.MaintenanceRecordRepository;
import com.markian.rentitup.MaintenanceRecord.MaintenanceRecordService;
import com.markian.rentitup.Utils.AwsS3Service;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public class MaintenanceRecordServiceImpl implements MaintenanceRecordService {
    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private final MaintenanceRecordMapper maintenanceRecordMapper;
    private final AwsS3Service awsS3Service;
    private final MachineRepository machineRepository;
    private final MachineService machineService;

    public MaintenanceRecordServiceImpl(MaintenanceRecordRepository maintenanceRecordRepository,
                                        MaintenanceRecordMapper maintenanceRecordMapper,
                                        AwsS3Service awsS3Service,
                                        MachineRepository machineRepository,
                                        MachineService machineService) {
        this.maintenanceRecordRepository = maintenanceRecordRepository;
        this.maintenanceRecordMapper = maintenanceRecordMapper;
        this.awsS3Service = awsS3Service;
        this.machineRepository = machineRepository;
        this.machineService = machineService;
    }

    @Override
    public MaintenanceRecordResponse addRecordMetadata(Long machineId, MaintenanceRecordRequest request) {
        try {
            MaintenanceRecord maintenanceRecord = maintenanceRecordMapper.toEntity(request);

            Machine machine = findMachine(machineId);
            maintenanceRecord.setMachine(machine);

            MaintenanceRecord savedRecord = maintenanceRecordRepository.save(maintenanceRecord);
            return maintenanceRecordMapper.toResponse(savedRecord);
        } catch (MachineException e) {
            throw e;
        } catch (Exception e) {
            throw new MaintenanceRecordException("Error while saving record metadata: " + e.getMessage(), e);
        }
    }

    @Override
    public String saveRecordImage(Long id, MultipartFile imageRecord) {
        try {
            MaintenanceRecord maintenanceRecord = maintenanceRecordRepository.findById(id).orElseThrow(
                    () -> new MaintenanceRecordException("No record of id " + id + "found")
            );
            if (imageRecord == null || imageRecord.isEmpty()) {
                throw new MaintenanceRecordException("No image file provided");
            }
            String imageUrl = awsS3Service.saveImageToS3(imageRecord, "maintenanceRecord/");
            maintenanceRecord.setImageRecordUrl(imageUrl);
            maintenanceRecordRepository.save(maintenanceRecord);
            return "Image uploaded succesfully";
        } catch (Exception e) {
            throw new MaintenanceRecordException("Error while saving image: " + e.getMessage(), e);
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

    @Override
    public List<MaintenanceRecordResponse> getUncheckedRecords() {
        return maintenanceRecordRepository.findByCheckedFalseOrderByCreatedAtDesc()
                .stream()
                .map(maintenanceRecordMapper::toResponse)
                .toList();
    }

    private Machine findMachine(Long machineId) {
        return machineRepository.findById(machineId)
                .orElseThrow(() -> new MachineException("Machine not found with ID: " + machineId));
    }

    @Override
    @Transactional
    public MaintenanceRecordResponse verifyMaintenanceRecord(Long recordId) throws MaintenanceRecordException {
        try {

            MaintenanceRecord record = maintenanceRecordRepository.findById(recordId)
                    .orElseThrow(() -> new MaintenanceRecordException("Record not found"));

            record.setChecked(Boolean.TRUE);
            MaintenanceRecord savedRecord = maintenanceRecordRepository.save(record);
            maintenanceRecordRepository.flush();
            machineService.verifyMachine(record.getMachine().getId());
            MaintenanceRecordResponse response = maintenanceRecordMapper.toResponse(savedRecord);
            return response;
        } catch (MachineException e) {
            throw new MaintenanceRecordException("Failed to verify machine: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new MaintenanceRecordException("Failed to verify maintenance record: " + e.getMessage(), e);
        }
    }

}
