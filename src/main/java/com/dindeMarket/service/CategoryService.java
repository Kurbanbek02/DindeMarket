package com.dindeMarket.service;

import com.dindeMarket.api.payload.CategoryRequest;
import com.dindeMarket.api.payload.CategoryResponse;
import com.dindeMarket.api.payload.SubcategoryResponse;
import com.dindeMarket.db.entity.CategoryEntity;
import com.dindeMarket.db.entity.SubcategoryEntity;
import com.dindeMarket.db.entity.UserEntity;
import com.dindeMarket.db.repository.CategoryRepository;
import com.dindeMarket.db.repository.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final SubcategoryService subcategoryService;

    public List<CategoryResponse> getAllCategories(UserEntity user) {
        return categoryRepository.findAllByFavorites(user.getRegion().getId())
                .stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());
    }

    public Optional<CategoryResponse> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::convertToCategoryResponse);
    }

    public CategoryResponse saveCategory(CategoryRequest categoryRequest, UserEntity user) {
        CategoryEntity category = new CategoryEntity();
        category.setName(categoryRequest.getName());
        category.setPhoto(categoryRequest.getPhoto());
        category.setFavorites(categoryRequest.getFavorites());
        category.setCreationDate(categoryRequest.getCreationDate());
        category.setRegion(user.getRegion());
        CategoryEntity savedCategory = categoryRepository.save(category);
        List<SubcategoryEntity> subcategories = new ArrayList<>();
        // Добавление подкатегорий, если они существуют
        if (categoryRequest.getSubсategories() != null && !categoryRequest.getSubсategories().isEmpty()) {
            for (String name : categoryRequest.getSubсategories()) {
                SubcategoryEntity entity = new SubcategoryEntity();
                entity.setName(name);
                entity.setCategory(savedCategory);
                subcategoryRepository.save(entity); // Сохранение подкатегории
                savedCategory.getSubcategories().add(entity); // Добавление в категорию
            }
        }
        return convertToCategoryResponse(savedCategory);
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(categoryRequest.getName());
        category.setPhoto(categoryRequest.getPhoto());
        category.setFavorites(categoryRequest.getFavorites());
        category.setCreationDate(categoryRequest.getCreationDate());

        CategoryEntity updatedCategory = categoryRepository.save(category);
        return convertToCategoryResponse(updatedCategory);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    public void deleteCategoriesByIds(List<Long> ids) {
        categoryRepository.deleteAllById(ids);
    }

    public CategoryResponse saveCategoryWithSubcategory(CategoryEntity category) {
        CategoryEntity savedCategory = categoryRepository.save(category);
        return convertToCategoryResponse(savedCategory);
    }

    public List<CategoryResponse> getFavoriteCategories(UserEntity user) {
        return categoryRepository.findFavorites(user.getRegion().getId())
                .stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());
    }
    private CategoryResponse convertToCategoryResponse(CategoryEntity categoryEntity) {
        return CategoryResponse.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .photo(categoryEntity.getPhoto())
                .favorites(categoryEntity.getFavorites())
                .creationDate(categoryEntity.getCreationDate())
                .subcategories(categoryEntity.getSubcategories().stream()
                        .map(subcategoryService::convertToResponse) // Используем метод convertToResponse для преобразования
                        .collect(Collectors.toList()))
                .build();
    }

}
