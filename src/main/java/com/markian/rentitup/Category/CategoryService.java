package com.markian.rentitup.Category;

import com.markian.rentitup.Category.Dto.CategoryListResponse;
import com.markian.rentitup.Category.Dto.CategoryRequestDto;
import com.markian.rentitup.Category.Dto.CategoryResponseDto;
import com.markian.rentitup.Exceptions.CategoryException;

import java.util.List;

public interface CategoryService {

    List<CategoryListResponse> getAllCategories() ;

    CategoryResponseDto createCategory(CategoryRequestDto categoryDto) throws CategoryException;

    CategoryResponseDto getCategoryById(Long id) throws CategoryException;


    String updateCategory(Long id, CategoryRequestDto categoryDto) throws CategoryException;

    String deleteCategory(Long id) throws CategoryException;
}
