package com.markian.rentitup.Category.Dto;

import com.markian.rentitup.Category.Category;
import com.markian.rentitup.Category.PriceCalculationType;
import com.markian.rentitup.Machine.MachineDto.MachineMapper;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CategoryMapper {
    private final MachineMapper machineMapper;

    public CategoryMapper(MachineMapper machineMapper) {
        this.machineMapper = machineMapper;
    }


    public  CategoryResponseDto toCategoryResponseDto(Category category) {
        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getPriceCalculationType(),
                category.getMachines() != null ? category.getMachines().stream()
                        .map(machineMapper::toListResponseDto)
                        .toList()
                        : null
        );
    }

    public CategoryListResponse toCategoryListResponseDto(Category category) {
        return new CategoryListResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    public Category toCategory(CategoryRequestDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setPriceCalculationType(PriceCalculationType.valueOf(dto.getPriceCalculationType()));
        return category;
    }
}
