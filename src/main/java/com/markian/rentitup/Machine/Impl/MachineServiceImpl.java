package com.markian.rentitup.Machine.Impl;

import com.markian.rentitup.Category.Category;
import com.markian.rentitup.Category.CategoryRepository;
import com.markian.rentitup.Config.EmailService;
import com.markian.rentitup.Exceptions.CategoryException;
import com.markian.rentitup.Exceptions.MachineException;
import com.markian.rentitup.Exceptions.UserException;
import com.markian.rentitup.Machine.Machine;
import com.markian.rentitup.Machine.MachineCondition;
import com.markian.rentitup.Machine.MachineDto.MachineListResponseDto;
import com.markian.rentitup.Machine.MachineDto.MachineMapper;
import com.markian.rentitup.Machine.MachineDto.MachineRequestDto;
import com.markian.rentitup.Machine.MachineDto.MachineResponseDto;
import com.markian.rentitup.Machine.MachineRepository;
import com.markian.rentitup.Machine.MachineService;
import com.markian.rentitup.User.Role;
import com.markian.rentitup.User.User;
import com.markian.rentitup.User.UserRepository;
import com.markian.rentitup.Utils.AwsS3Service;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class MachineServiceImpl implements MachineService {

    private final MachineRepository machineRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final MachineMapper machineMapper;
    private final AwsS3Service awsS3Service;
    private final Logger logger = LoggerFactory.getLogger(MachineServiceImpl.class);
    private final EmailService emailService;

    public MachineServiceImpl(MachineRepository machineRepository, UserRepository userRepository, CategoryRepository categoryRepository, MachineMapper machineMapper, AwsS3Service awsS3Service, EmailService emailService) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.machineMapper = machineMapper;
        this.awsS3Service = awsS3Service;
        this.emailService = emailService;
    }


    @Override
    public List<MachineListResponseDto> getAllMachines() {
        return machineRepository.findAll()
                .stream()
                .map(machineMapper::toListResponseDto)
                .toList();
    }

    @Override
    public MachineResponseDto getMachineId(Long id) throws MachineException {
        try {
            Machine machine = machineRepository.findById(id).orElseThrow(
                    () -> new MachineException("Could not find machine by id: " + id)
            );
            return machineMapper.toResponseDto(machine);

        } catch (MachineException e) {
            throw e;
        } catch (Exception e) {
            throw new MachineException("Error getting machine by id" + e.getMessage(), e);
        }
    }

    @Override
    public String changeAvailability(Long id) throws MachineException {
        try {
            Machine machine = machineRepository.findById(id).orElseThrow(
                    () -> new MachineException("Could not find machine by id: " + id)
            );
            machine.setIsAvailable(!machine.getIsAvailable());
            machineRepository.save(machine);
            return "Availability changed to " + machine.getIsAvailable();

        } catch (Exception e) {
            throw new MachineException("unable to change availability " + e.getMessage(), e);
        }
    }

    @Override
    public MachineResponseDto createMachine(MachineRequestDto machineRequestDto) {
        try {
            Category category = categoryRepository.findById(machineRequestDto.getCategoryId())
                    .orElseThrow(() -> new MachineException("Category not found with id: " + machineRequestDto.getCategoryId()));

            User user = userRepository.findById(machineRequestDto.getOwnerId())
                    .orElseThrow(() -> new UserException("Owner of id not found"));

            if (user.getRole() != Role.OWNER) {
                throw new MachineException("user is not owner ");
            }

            Machine machine = machineMapper.toEntity(machineRequestDto);
            machine.setCategory(category);
            machine.setOwner(user);
            machine.setIsAvailable(true);

            return machineMapper.toResponseDto(
                    machineRepository.save(machine)
            );

        } catch (MachineException e) {
            throw e;
        } catch (Exception e) {
            throw new MachineException("Error while creating a machine " + e.getMessage(), e);
        }
    }

    @Override
    public String updateMachine(Long id, MachineRequestDto machineRequestDto) throws MachineException {
        try {
            MachineCondition machineCondition;
            try {
                machineCondition = MachineCondition.valueOf(machineRequestDto.getCondition());

            } catch (IllegalArgumentException e) {
                throw new MachineException("Invalid condition", e);
            }
            Machine machine = machineRepository.findById(id).orElseThrow(
                    () -> new MachineException("Could not find machine of id " + id)
            );
            Category category = categoryRepository.findById(machineRequestDto.getCategoryId())
                    .orElseThrow(() -> new MachineException("Category not found with id: " + machineRequestDto.getCategoryId()));

            machine.setName(machineRequestDto.getName());
            machine.setDescription(machineRequestDto.getDescription());
            machine.setBasePrice(machineRequestDto.getBasePrice());
            machine.setCondition(machineCondition);
            machine.setSpecification(machineRequestDto.getSpecification());
            machine.setCategory(category);

            machineRepository.save(machine);
            return "Machine updated successfully";
        } catch (MachineException e) {
            throw e;
        } catch (Exception e) {
            throw new MachineException("Error while updating the machine " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public String deleteMachine(Long id) throws MachineException {
        try {
            Machine machine = machineRepository.findById(id)
                    .orElseThrow(() -> new MachineException("Machine not found with id: " + id));
            machineRepository.delete(machine);
            return "Machine deleted successfully";
        } catch (MachineException e) {
            throw e;
        } catch (Exception e) {
            throw new MachineException("Error while deleting the machine: " + e.getMessage(), e);
        }
    }


    @Override
    public List<MachineListResponseDto> getAllByCategory(Long categoryId) {
        try {
            categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CategoryException("Category not found with id: " + categoryId));

            List<Machine> machines = machineRepository.findByCategory_Id(categoryId);

            if (machines.isEmpty()) {
                return Collections.emptyList();
            }

            return machines.stream()
                    .map(machineMapper::toListResponseDto)
                    .toList();

        } catch (CategoryException e) {
            throw e;
        } catch (Exception e) {
            throw new MachineException("Error getting machines by category: " + e.getMessage(), e);
        }
    }

    @Override
    public List<MachineListResponseDto> getAllByCondition(Long categoryId, String condition) {
        try {
            MachineCondition machineCondition;
            try {
                machineCondition = MachineCondition.valueOf(condition.toUpperCase());

            } catch (IllegalArgumentException e) {
                throw new MachineException("Invalid condition" + condition);
            }
            List<Machine> machines = machineRepository.findAllByCategory_IdAndCondition(categoryId, machineCondition);
            if (machines.isEmpty()) {
                return Collections.emptyList();
            }
            return machines.stream()
                    .map(machineMapper::toListResponseDto)
                    .toList();

        } catch (Exception e) {
            throw new MachineException("Error getting machines by condition" + e.getMessage(), e);
        }


    }

    @Override
    public List<MachineListResponseDto> getAllByAvailability(Long categoryId) {
        try {
            categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CategoryException("Category not found with id: " + categoryId));

            List<Machine> machines = machineRepository
                    .findAllByCategory_IdAndIsAvailable(categoryId, true);

            if (machines.isEmpty()) {
                return Collections.emptyList();
            }

            return machines.stream()
                    .map(machineMapper::toListResponseDto)
                    .toList();

        } catch (CategoryException e) {
            throw e;
        } catch (Exception e) {
            throw new MachineException("Error getting available machines: " + e.getMessage(), e);
        }
    }

    @Override
    public List<MachineListResponseDto> getAllBySearch(String nameOfMachine) {
        try {
            if (nameOfMachine == null && nameOfMachine.trim().isEmpty()) {
                throw new MachineException("Search term cannot be empty");
            }

            List<Machine> machines = machineRepository.findAllByNameContainingIgnoreCase(nameOfMachine.trim());
            if (machines.isEmpty()) {
                return Collections.emptyList();
            }
            return machines.stream()
                    .map(machineMapper::toListResponseDto)
                    .toList();
        } catch (MachineException e) {
            throw e;
        } catch (Exception e) {
            throw new MachineException("Error getting machines with the names " + e.getMessage(), e);
        }
    }

    @Override
    public List<MachineListResponseDto> getAllByOwner(Long ownerId) {
        try {
            User user = userRepository.findById(ownerId).orElseThrow(
                    () -> new UserException("User with the id " + ownerId + " not found ")
            );

            if (user.getRole() != Role.OWNER) {
                throw new MachineException("user is not owner ");
            }
            List<Machine> machines = machineRepository.findAllByOwnerId(ownerId);
            return machines.stream()
                    .map(machineMapper::toListResponseDto)
                    .toList();
        } catch (UserException | MachineException e) {
            throw e;
        } catch (Exception e) {
            throw new MachineException("Error getting machines by the owner " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> getMachineCondition() {
        return Arrays.asList(
                MachineCondition.EXCELLENT.name(),
                MachineCondition.GOOD.name(),
                MachineCondition.FAIR.name()
        );
    }

    @Override
    @Transactional
    public String verifyMachine(Long machineId) throws MachineException {
        try{
            LocalDateTime verificationTime = LocalDateTime.now();
            LocalDateTime deadline = verificationTime.plusMonths(3);

            machineRepository.verifyMachine(Boolean.TRUE,machineId, verificationTime,deadline);
            Machine machine = machineRepository.findById(machineId).orElseThrow(
                    ()->new MachineException("Machine with id " + machineId + "not found")
            );

            Map<String,Object> templateVariables = new HashMap<>();

            templateVariables.put("ownerName", machine.getOwner().getFullName());
            templateVariables.put("status",machine.getVerified());
            templateVariables.put("verifiedAt",machine.getUpdatedAt());
            templateVariables.put("machineName",machine.getName());
            templateVariables.put("message","The Machine has been verified ");

            CompletableFuture.runAsync(
                    () ->  emailService.sendEmail(
                            machine.getOwner().getEmail(),
                            "Verification of machine",
                            "machine-verification",
                            templateVariables
                    )
            ).exceptionally(throwable -> {
                log.error("Failed to send verification email for machine ");
                return null;
            });
            return "Machine has been verified ";

        }catch (Exception e) {
            throw new MachineException("Unable to verify machine" +e.getMessage(),e);
        }
    }
}

