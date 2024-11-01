package com.markian.rentitup.Category.Dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.markian.rentitup.Category.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryRequestDto {

    @NotBlank(message = "Category name is required")
    private String name;

    private String description;

    @NotNull(message = "Price calculation type is required")
    private Category.PriceCalculationType priceCalculationType;
}

