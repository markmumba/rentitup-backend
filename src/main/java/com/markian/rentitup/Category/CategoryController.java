package com.markian.rentitup.Category;

import com.markian.rentitup.Category.Dto.CategoryListResponse;
import com.markian.rentitup.Category.Dto.CategoryRequestDto;
import com.markian.rentitup.Category.Dto.CategoryResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryListResponse>> getAllCategories() {
        List<CategoryListResponse> categoryListResponse = categoryService.getAllCategories();
        return ResponseEntity.ok(categoryListResponse);
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(
            @RequestBody CategoryRequestDto categoryRequestDto
    ) {
        CategoryResponseDto categoryResponseDto = categoryService.createCategory(categoryRequestDto);
        return ResponseEntity.ok(categoryResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable("id") Long id) {
        CategoryResponseDto categoryResponseDto = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryResponseDto);
    }

    @GetMapping("/calculation-types")
    public ResponseEntity<List<String>> getCalculationTypes() {
        return ResponseEntity.ok(categoryService.getPriceCalculationType());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(
            @PathVariable Long id,
            @RequestBody  CategoryRequestDto categoryRequestDto
    ) {

        return ResponseEntity.ok(categoryService.updateCategory(id,categoryRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

}
