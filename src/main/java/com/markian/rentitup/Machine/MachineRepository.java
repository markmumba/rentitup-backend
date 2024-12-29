package com.markian.rentitup.Machine;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MachineRepository extends JpaRepository<Machine, Long> {
    List<Machine> findByCategory_Id(Long categoryId);

    List<Machine> findAllByCategory_IdAndCondition(Long categoryId, MachineCondition condition);

    List<Machine> findAllByCategory_IdAndIsAvailable(Long categoryId, boolean isAvailable);

    List<Machine> findAllByNameContainingIgnoreCase(String nameOfMachine);

    List<Machine> findAllByOwnerId(Long ownerId);

    @Modifying
    @Query("UPDATE machine m SET m.verified = :verified ,m.updatedAt= :time WHERE m.id =:machineId")
    void verifyMachine(@Param("verified") Boolean verified, @Param("machineId") Long machineId, @Param("time") LocalDateTime time);

}

