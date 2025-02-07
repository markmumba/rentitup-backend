package com.markian.rentitup.Machine.MachineDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.markian.rentitup.Machine.MachineAvailability;
import com.markian.rentitup.Machine.MachineCondition;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MachineListResponseDto {

    private Long id;

    private String name;

    private BigDecimal basePrice;

    private String description;

    private Boolean verified;

    private MachineAvailability status;

    private Boolean isAvailable;

    private String machineImageUrl;

    private MachineCondition condition;

    private Long categoryId;

}
