package com.markian.rentitup.MaintenanceRecord;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {
    List<MaintenanceRecord> findAllByMachine_IdOrderByServiceDate(Long machineId);

    List<MaintenanceRecord> findByCheckedFalseOrderByCreatedAtDesc();

}
