package com.markian.rentitup.Machine.MachineDto;


import com.markian.rentitup.Machine.MachineCondition;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MachineRequestDto {

    @NotNull
    private String name;

    private String description;

    @NotNull
    private BigDecimal basePrice;

    @NotNull
    private MachineCondition condition;

    private String specification;

    @NotNull
    private Long categoryId;

    @NotNull
    private Long ownerId;

}

