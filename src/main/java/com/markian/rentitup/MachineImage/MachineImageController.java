package com.markian.rentitup.MachineImage;

import com.markian.rentitup.MachineImage.MachineImageDto.MachineImageResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//

@RestController
@RequestMapping("/api/v1/machines/{machineId}/images")
public class MachineImageController {

    private final MachineImageService machineImageService;

    public MachineImageController(MachineImageService machineImageService) {
        this.machineImageService = machineImageService;
    }


    @PostMapping
    public ResponseEntity<String> uploadMachineImages(
            @PathVariable(value = "machineId", required = true) Long machineId,
            @RequestParam(name = "images", required = true) MultipartFile[] images
    ) {
        String response = machineImageService.addImages(machineId, images);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MachineImageResponseDto>> getAllMachineImages(
            @PathVariable(value = "machineId", required = true) Long machineId
    ){
       List<MachineImageResponseDto>  machineImages = machineImageService.getAllMachineImages(machineId);
       return ResponseEntity.ok(machineImages);
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<MachineImageResponseDto> getMachineImage(
            @PathVariable(value = "machineId", required = true) Long machineId,
            @PathVariable(value = "imageId", required = true) Long imageId
    ){
        MachineImageResponseDto machineImage = machineImageService.getMachineImage(machineId,imageId);
        return ResponseEntity.ok(machineImage);
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<String> deleteMachineImage(
            @PathVariable(value = "machineId", required = true) Long machineId,
            @PathVariable(value = "imageId", required = true) Long imageId
    ){
        String response = machineImageService.deleteMachineImage(machineId,imageId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{imageId}/primary")
    public ResponseEntity<String> setIsPrimary (
            @PathVariable Long machineId,
            @PathVariable Long imageId
    ) {
        machineImageService.setIsPrimary(machineId, imageId);
        return ResponseEntity.ok("Primary image updated successfully");
    }
}
