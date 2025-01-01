package com.markian.rentitup.Machine;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface MachineRepository extends JpaRepository<Machine, Long> {
    List<Machine> findByCategory_Id(Long categoryId);

    List<Machine> findAllByCategory_IdAndCondition(Long categoryId, MachineCondition condition);

    List<Machine> findAllByCategory_IdAndIsAvailable(Long categoryId, boolean isAvailable);

    List<Machine> findAllByNameContainingIgnoreCase(String nameOfMachine);

    List<Machine> findAllByOwnerId(Long ownerId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE machine m SET m.verified = :verified ,m.updatedAt= :time, m.verificationDeadline= :deadline WHERE m.id =:machineId")
    void verifyMachine(@Param("verified") Boolean verified, @Param("machineId") Long machineId, @Param("time") LocalDateTime time,@Param("deadline") LocalDateTime deadline);

    @Query("SELECT m FROM machine m WHERE m.verified = true AND m.verificationDeadline < :currentTime")
    List<Machine> findByVerifiedTrueAndVerificationDeadlineBefore(LocalDateTime currentTime);
}

