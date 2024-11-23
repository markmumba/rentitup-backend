package com.markian.rentitup.Machine;

import com.markian.rentitup.Machine.MachineDto.MachineListResponseDto;
import com.markian.rentitup.Machine.MachineDto.MachineRequestDto;
import com.markian.rentitup.Machine.MachineDto.MachineResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//TODO in  search if no match was found return none was found ... no match

@RestController
@RequestMapping("api/v1/machines")
public class MachineController {
    private final MachineService machineService;

    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<MachineListResponseDto>> getAllMachines() {
        List<MachineListResponseDto> machineListResponseDtoList =
                machineService.getAllMachines();
        return ResponseEntity.ok(machineListResponseDtoList);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<MachineListResponseDto>> getAllByOwner(@PathVariable Long ownerId) {
        List<MachineListResponseDto> machineListResponseDtoList = machineService.getAllByOwner(ownerId);
        return ResponseEntity.ok(machineListResponseDtoList);
    }



    @GetMapping("/{id}")
    public ResponseEntity<MachineResponseDto> getMachineById(@PathVariable Long id) {
        MachineResponseDto machineResponseDto =
                machineService.getMachineId(id);
        return ResponseEntity.ok(machineResponseDto);
    }

    @PreAuthorize("hasAuthority('OWNER')")
    @PostMapping
    public ResponseEntity<MachineResponseDto> createMachine(
            @RequestBody MachineRequestDto machineRequestDto
    ) {
        MachineResponseDto machineResponseDto = machineService.createMachine(machineRequestDto);
        return ResponseEntity.ok(machineResponseDto);
    }

    @PreAuthorize("hasAuthority('OWNER')")
    @PostMapping("{id}/change-availability")
    public ResponseEntity<String> changeAvailability(@PathVariable Long id) {
        String response = machineService.changeAvailability(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all-by-category")
    public ResponseEntity<List<MachineListResponseDto>> getAllByCategory(
            @RequestParam(name = "categoryId", required = true) Long categoryId
    ) {
        List<MachineListResponseDto> machineListResponseDtoList = machineService.getAllByCategory(categoryId);
        return ResponseEntity.ok(machineListResponseDtoList);
    }

    @GetMapping("/all-by-condition")
    public ResponseEntity<List<MachineListResponseDto>> getAllByCondition(
            @RequestParam(name = "categoryId", required = true) Long categoryId,
            @RequestParam(name = "condition", required = true) String condition

    ) {
        List<MachineListResponseDto> machineListResponseDtoList = machineService.getAllByCondition(categoryId, condition);
        return ResponseEntity.ok(machineListResponseDtoList);
    }

    @GetMapping("/all-by-availability")
    public ResponseEntity<List<MachineListResponseDto>> getAllByAvailability(
            @RequestParam(name = "categoryId", required = true) Long categoryId
    ){
        List<MachineListResponseDto> machineListResponseDtoList = machineService.getAllByAvailability(categoryId);
        return ResponseEntity.ok(machineListResponseDtoList);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MachineListResponseDto>> getAllBySearch(
            @RequestParam(name = "nameOfMachine", required = true)String nameOfMachine
    ) {
        List<MachineListResponseDto> machineListResponseDtoList  = machineService.getAllBySearch(nameOfMachine);
        return ResponseEntity.ok(machineListResponseDtoList);
    }

    @PreAuthorize("hasAuthority('OWNER')")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateMachine(
            @PathVariable("id") Long id,
            @RequestBody MachineRequestDto machineRequestDto
    ){
        String response = machineService.updateMachine(id,machineRequestDto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('OWNER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMachine (
            @PathVariable("id") Long id
    ) {
        String response = machineService.deleteMachine(id);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAuthority('OWNER')")
    @GetMapping("/machineConditions")
    public ResponseEntity<List<String>> getMachineConditions() {
        List<String> machineConditions = machineService.getMachineCondition();
        return ResponseEntity.ok(machineConditions);
    }
}
