package com.markian.rentitup.Machine;

import com.markian.rentitup.Exceptions.CategoryException;
import com.markian.rentitup.Exceptions.MachineException;
import com.markian.rentitup.Machine.MachineDto.MachineListResponseDto;
import com.markian.rentitup.Machine.MachineDto.MachineRequestDto;
import com.markian.rentitup.Machine.MachineDto.MachineResponseDto;

import java.util.List;

public interface MachineService {

    List<MachineListResponseDto> getAllMachines();

    MachineResponseDto getMachineId(Long id) throws MachineException;

    String changeAvailability(Long id) throws MachineException;

    MachineResponseDto createMachine(MachineRequestDto machineRequestDto);

    String updateMachine(Long id, MachineRequestDto machineRequestDto) throws MachineException;

    String deleteMachine(Long id) throws MachineException;

    List<MachineListResponseDto> getAllByCategory(Long categoryId) throws CategoryException;

    List<MachineListResponseDto> getAllByCondition(Long categoryId, String condition);

    List<MachineListResponseDto> getAllByAvailability(Long categoryId);

    List<MachineListResponseDto> getAllBySearch(String nameOfMachine);

    List<MachineListResponseDto> getAllByOwner(Long ownerId);

    List<String> getMachineCondition();
}