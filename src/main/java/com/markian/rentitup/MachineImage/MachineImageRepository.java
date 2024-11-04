package com.markian.rentitup.MachineImage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MachineImageRepository extends JpaRepository<MachineImage, Long> {

    List<MachineImage> findAllByMachineId(Long machineId);

    Optional<MachineImage> findByMachineIdAndImageId(Long machineId, Long imageId);

    boolean existsByMachineId(Long machineId);
}
