package com.markian.rentitup.Machine.Impl;

import com.markian.rentitup.Config.EmailService;
import com.markian.rentitup.Machine.Machine;
import com.markian.rentitup.Machine.MachineRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class MachineVerificationSchedular {

    private final MachineRepository machineRepository;
    private final EmailService emailService;

    public MachineVerificationSchedular(MachineRepository machineRepository, EmailService emailService) {
        this.machineRepository = machineRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void checkVerificationStatus() {

        List<Machine> expiredMachines =
                machineRepository.findByVerifiedTrueAndVerificationDeadlineBefore(LocalDateTime.now());
        for (Machine machine :expiredMachines ) {
            machine.setVerified(false);
            machine.setVerificationDeadline(null);
            machineRepository.save(machine);

            Map<String,Object > templateVariables = new HashMap<>();
            templateVariables.put("ownerName", machine.getOwner().getFullName());
            templateVariables.put("machineName", machine.getName());
            templateVariables.put("status",machine.getVerified());

            CompletableFuture.runAsync(() ->
                    emailService.sendEmail(
                            machine.getOwner().getEmail(),
                            "Machine Verification Expired",
                            "machine-verification-expired",
                            templateVariables
                    )
            ).exceptionally(throwable -> {
                log.error("Failed to send verification expiry email for machine {}: ",
                        machine.getId(), throwable);
                return null;
            });
        }

        log.info("Completed verification check. Updated {} machines", expiredMachines.size());
    }
}
