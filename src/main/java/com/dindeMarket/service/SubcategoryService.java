package com.dindeMarket.service;

import com.dindeMarket.api.payload.ProductResponse;
import com.dindeMarket.api.payload.SubCategoryRequest;
import com.dindeMarket.api.payload.SubcategoryResponse;
import com.dindeMarket.db.entity.CategoryEntity;
import com.dindeMarket.db.entity.ProductEntity;
import com.dindeMarket.db.entity.SubcategoryEntity;
import com.dindeMarket.db.repository.CategoryRepository;
import com.dindeMarket.db.repository.ProductRepository;
import com.dindeMarket.db.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubcategoryService {

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;
    @Autowired
    private  ProductRepository productRepository;

    public List<SubcategoryResponse> findAll() {
        return subcategoryRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Optional<SubcategoryResponse> findById(Long id) {
        return subcategoryRepository.findById(id)
                .map(this::convertToResponse);
    }

    public List<SubcategoryResponse> findByCategoryId(Long categoryId) {
        return subcategoryRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public SubcategoryResponse save(SubCategoryRequest subcategoryRequest) {
        SubcategoryEntity subcategory = new SubcategoryEntity();
        subcategory.setName(subcategoryRequest.getName());

        Optional<CategoryEntity> category = categoryRepository.findById(subcategoryRequest.getCategoryId());
        if (category.isPresent()) {
            subcategory.setCategory(category.get());
        } else {
            throw new RuntimeException("Category not found");
        }

        SubcategoryEntity savedSubcategory = subcategoryRepository.save(subcategory);
        return convertToResponse(savedSubcategory);
    }

    public SubcategoryResponse update(Long id, SubCategoryRequest subcategoryRequest) {
        Optional<SubcategoryEntity> subcategoryOptional = subcategoryRepository.findById(id);
        if (subcategoryOptional.isPresent()) {
            SubcategoryEntity subcategoryToUpdate = subcategoryOptional.get();
            subcategoryToUpdate.setName(subcategoryRequest.getName());

            Optional<CategoryEntity> category = categoryRepository.findById(subcategoryRequest.getCategoryId());
            if (category.isPresent()) {
                subcategoryToUpdate.setCategory(category.get());
            } else {
                throw new RuntimeException("Category not found");
            }

            SubcategoryEntity updatedSubcategory = subcategoryRepository.save(subcategoryToUpdate);
            return convertToResponse(updatedSubcategory);
        } else {
            throw new RuntimeException("Subcategory not found");
        }
    }

    public void deleteById(Long id) {
        subcategoryRepository.deleteById(id);
    }

    public List<SubcategoryResponse> searchProductsByNameGroupedBySubcategory(String name) {
        // Находим все продукты по имени
        List<ProductEntity> products = productRepository.findByNameContainingIgnoreCase(name);

        // Группируем продукты по подкатегориям
        Map<SubcategoryEntity, List<ProductEntity>> groupedProducts = products.stream()
                .collect(Collectors.groupingBy(ProductEntity::getSubcategory));

        // Преобразуем группы в SubcategoryResponse
        return groupedProducts.entrySet().stream()
                .map(entry -> {
                    SubcategoryEntity subcategory = entry.getKey();
                    List<ProductEntity> productEntities = entry.getValue();

                    // Устанавливаем продукты в подкатегорию
                    subcategory.setProducts(productEntities);

                    // Преобразуем подкатегорию с продуктами в SubcategoryResponse
                    return convertToResponse(subcategory);
                })
                .collect(Collectors.toList());
    }

    public SubcategoryResponse convertToResponse(SubcategoryEntity entity) {
        SubcategoryResponse response = new SubcategoryResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        // Преобразуем список продуктов
        List<ProductResponse> productResponses = entity.getProducts().stream()
                .filter(p-> !p.getHidden())
                .map(productService::convertToResponse)  // Преобразуем продукты
                .collect(Collectors.toList());

        response.setProducts(productResponses);
        return response;
    }
}
