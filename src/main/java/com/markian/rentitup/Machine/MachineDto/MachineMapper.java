package com.markian.rentitup.Machine.MachineDto;

import com.markian.rentitup.Machine.Machine;

public interface MachineMapper  {
    MachineResponseDto toResponseDto(Machine machine);
    MachineListResponseDto toListResponseDto(Machine machine);
    Machine toEntity(MachineRequestDto dto);
}
