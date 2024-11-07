package com.markian.rentitup.MachineImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MachineImageRepository extends JpaRepository<MachineImage, Long> {

    List<MachineImage> findAllByMachineId(Long machineId);

    Optional<MachineImage> findByMachineIdAndId(Long machineId, Long imageId);

    boolean existsByMachineId(Long machineId);

    boolean existsByMachineIdAndIsPrimaryTrue(Long machineId);

    @Modifying
    @Query("UPDATE MachineImage m SET m.isPrimary = false WHERE m.machine.id = :machineId")
    void updateAllPrimaryToFalse(Long machineId);
}
