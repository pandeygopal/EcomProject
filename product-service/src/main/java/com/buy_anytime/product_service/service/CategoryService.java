package com.buy_anytime.product_service.service;



import com.buy_anytime.product_service.dto.category.CategoryResponseDto;
import com.buy_anytime.product_service.dto.category.CreateCategoryRequestDto;

import java.util.List;

public interface CategoryService {
    CategoryResponseDto createCategory(CreateCategoryRequestDto requestDto);
    CategoryResponseDto updateCategory(String id, CreateCategoryRequestDto requestDto);
    void deleteCategory(String id);
    CategoryResponseDto getCategoryById(String id);
    List<CategoryResponseDto> getAllCategories();
    List<CategoryResponseDto> getRootCategories(); // Danh mục gốc cho mega menu
}
