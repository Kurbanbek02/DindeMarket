package com.dindeMarket.api.controller;

import com.dindeMarket.api.payload.SubCategoryRequest;
import com.dindeMarket.api.payload.SubcategoryResponse;
import com.dindeMarket.db.entity.SubcategoryEntity;
import com.dindeMarket.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/subcategories")
@PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_CLIENT')")
@CrossOrigin
public class SubcategoryController {
    @Autowired
    private SubcategoryService subcategoryService;

    @GetMapping
    public List<SubcategoryResponse> getAllSubcategories() {
        return subcategoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubcategoryResponse> getSubcategoryById(@PathVariable Long id) {
        Optional<SubcategoryResponse> subcategory = subcategoryService.findById(id);
        if (subcategory.isPresent()) {
            return ResponseEntity.ok(subcategory.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/category/{categoryId}")
    public List<SubcategoryResponse> getSubcategoriesByCategoryId(@PathVariable Long categoryId) {
        return subcategoryService.findByCategoryId(categoryId);
    }

    @PostMapping
    public SubcategoryResponse createSubcategory(@RequestBody SubCategoryRequest subcategoryRequest) {
        return subcategoryService.save(subcategoryRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubcategoryResponse> updateSubcategory(@PathVariable Long id, @RequestBody SubCategoryRequest subcategoryRequest) {
        try {
            SubcategoryResponse updatedSubcategory = subcategoryService.update(id, subcategoryRequest);
            return ResponseEntity.ok(updatedSubcategory);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubcategory(@PathVariable Long id) {
        subcategoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/search")
    public ResponseEntity<List<SubcategoryResponse>> searchProductsByName(@RequestParam String name) {
        // Вызываем сервисный метод для поиска и группировки продуктов
        List<SubcategoryResponse> subcategoryResponses = subcategoryService.searchProductsByNameGroupedBySubcategory(name);

        // Возвращаем результат с кодом 200 OK
        return ResponseEntity.ok(subcategoryResponses);
    }
}

