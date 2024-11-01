package com.markian.rentitup.Category.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.markian.rentitup.Category.Category;
import com.markian.rentitup.Machine.MachineDto.MachineResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class CategoryResponseDto {

    private Long id;
    private String name;
    private String description;
    private Category.PriceCalculationType priceCalculationType;
    private List<MachineResponseDto> machines;
}
