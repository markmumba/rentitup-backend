package com.markian.rentitup.MachineImage.MachineImageDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MachineImageResponseDto {
    private Long id;
    private String url;
    private Boolean isPrimary;
}
