package com.markian.rentitup.Machine.MachineDto;

import com.markian.rentitup.Machine.Machine;
import org.springframework.stereotype.Service;

@Service
public class MachineMapperImpl  implements  MachineMapper{

    public Machine toEntity(MachineRequestDto dto) {
        Machine machine = new Machine();
        machine.setName(dto.getName());
        machine.setDescription(dto.getDescription());
        machine.setBasePrice(dto.getBasePrice());
        machine.setSpecification(dto.getSpecification());
        machine.setCondition(dto.getCondition());
        return machine;
    }

    public MachineResponseDto toResponseDto(Machine machine) {
        MachineResponseDto responseDto = new MachineResponseDto();
        responseDto.setId(machine.getId());
        responseDto.setName(machine.getName());
        responseDto.setDescription(machine.getDescription());
        responseDto.setBasePrice(machine.getBasePrice());
        responseDto.setSpecification(machine.getSpecification());
        responseDto.setIsAvailable(machine.getIsAvailable());
        responseDto.setCondition(machine.getCondition());

        if (machine.getCategory() != null) {
            responseDto.setCategoryId(machine.getCategory().getId());
        }

        return responseDto;
    }

    public MachineListResponseDto toListResponseDto(Machine machine) {
        MachineListResponseDto responseDto = new MachineListResponseDto();
        responseDto.setId(machine.getId());
        responseDto.setName(machine.getName());
        responseDto.setBasePrice(machine.getBasePrice());
        responseDto.setIsAvailable(machine.getIsAvailable());
        responseDto.setCondition(machine.getCondition());

        if (machine.getCategory() != null) {
            responseDto.setCategoryId(machine.getCategory().getId());
        }


        return responseDto;
    }
}