package com.markian.rentitup.Machine;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MachineRepository extends JpaRepository<Machine, Long> {
    List<Machine> findByCategory_Id(Long categoryId);

    List<Machine> findAllByCategory_IdAndCondition(Long categoryId,MachineCondition condition);

    List<Machine> findAllByCategory_IdAndIsAvailable(Long categoryId, boolean isAvailable);

    List<Machine> findAllByNameContainingIgnoreCase( String nameOfMachine);
}

