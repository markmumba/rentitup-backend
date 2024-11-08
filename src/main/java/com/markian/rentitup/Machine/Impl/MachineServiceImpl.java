package com.markian.rentitup.Machine.Impl;

import com.markian.rentitup.Category.Category;
import com.markian.rentitup.Category.CategoryRepository;
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
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MachineServiceImpl implements MachineService {

    private final MachineRepository machineRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final MachineMapper machineMapper;

    public MachineServiceImpl(MachineRepository machineRepository, UserRepository userRepository, CategoryRepository categoryRepository, MachineMapper machineMapper) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.machineMapper = machineMapper;
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
        Machine machine = machineRepository.findById(id).orElseThrow(
                () -> new MachineException("Could not find machine by id: " + id)
        );
        try {
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
            Machine machine = machineRepository.findById(id).orElseThrow(
                    () -> new MachineException("Could not find machine of id " + id)
            );
            Category category = categoryRepository.findById(machineRequestDto.getCategoryId())
                    .orElseThrow(() -> new MachineException("Category not found with id: " + machineRequestDto.getCategoryId()));

            machine.setName(machineRequestDto.getName());
            machine.setDescription(machineRequestDto.getDescription());
            machine.setBasePrice(machineRequestDto.getBasePrice());
            machine.setCondition(machineRequestDto.getCondition());
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
    public String deleteMachine(Long id) throws MachineException {
        try {
            Machine machine = machineRepository.findById(id).orElseThrow(
                    () -> new MachineException("Could not find machine of id " + id)
            );
            machineRepository.delete(machine);
            return "Machine deleted successfully";

        } catch (MachineException e) {
            throw e;
        } catch (Exception e) {
            throw new MachineException("Error while deleting the machine", e);
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
}

