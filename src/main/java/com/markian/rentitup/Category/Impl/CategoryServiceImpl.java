package com.markian.rentitup.Category.Impl;

import com.markian.rentitup.Category.Category;
import com.markian.rentitup.Category.CategoryRepository;
import com.markian.rentitup.Category.CategoryService;
import com.markian.rentitup.Category.Dto.CategoryListResponse;
import com.markian.rentitup.Category.Dto.CategoryMapper;
import com.markian.rentitup.Category.Dto.CategoryRequestDto;
import com.markian.rentitup.Category.Dto.CategoryResponseDto;
import com.markian.rentitup.Category.PriceCalculationType;
import com.markian.rentitup.Exceptions.CategoryException;
import com.markian.rentitup.Exceptions.MachineException;
import com.markian.rentitup.Machine.Machine;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private static final Logger LOGGER = LogManager.getLogger();


    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }


    @Override
    public List<CategoryListResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toCategoryListResponseDto)
                .toList();
    }

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) throws CategoryException {
        try {

            if (categoryRepository.existsByName(categoryRequestDto.getName())) {
                throw new CategoryException("The category already exists");
            }
            Category category = categoryMapper.toCategory(categoryRequestDto);
            return categoryMapper.toCategoryResponseDto(
                    categoryRepository.save(category)
            );
        } catch (CategoryException e) {
            throw e;
        } catch (Exception e) {
            throw new CategoryException("Error occurred when creating category ", e);
        }
    }


    @Override
    public CategoryResponseDto getCategoryById(Long id) throws CategoryException {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new CategoryException("could not find category by  " + id));

            return categoryMapper.toCategoryResponseDto(category);
        } catch (CategoryException e) {
            throw e;
        } catch (Exception e) {
            throw new CategoryException("Error getting category by id " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> getPriceCalculationType() {
        return Arrays.asList(
                PriceCalculationType.HOURLY.name(),
                PriceCalculationType.DAILY.name(),
                PriceCalculationType.DISTANCE_BASED.name()
        );
    }

    @Override
    public String updateCategory(Long id, CategoryRequestDto categoryRequestDto) throws CategoryException {

        try {
            PriceCalculationType priceCalculationType;
            try {
                priceCalculationType = PriceCalculationType.valueOf(categoryRequestDto.getPriceCalculationType());

            } catch (IllegalArgumentException e) {
                throw new CategoryException("Invalid price type", e);
            }
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new CategoryException("could not find category by  " + id));
            category.setName(categoryRequestDto.getName());
            category.setDescription(categoryRequestDto.getDescription());
            category.setPriceCalculationType(priceCalculationType);
            categoryRepository.save(category);

            return "Category updated successful";
        } catch (CategoryException e) {
            throw e;
        } catch (Exception e) {
            throw new CategoryException("Unable to update category " + e.getMessage(), e);
        }
    }

    @Override
    public String deleteCategory(Long id) throws CategoryException {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new CategoryException("could not find category by  " + id));
            categoryRepository.delete(category);
            return "category deleted successfully";

        } catch (CategoryException e) {
            throw e;
        } catch (Exception e) {
            throw new CategoryException("Unable to delete category " + e.getMessage(), e);
        }
    }
}
