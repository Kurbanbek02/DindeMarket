package com.dindeMarket.api.controller;

import com.dindeMarket.api.payload.CategoryRequest;
import com.dindeMarket.api.payload.CategoryResponse;
import com.dindeMarket.db.entity.CategoryEntity;
import com.dindeMarket.db.entity.UserEntity;
import com.dindeMarket.db.repository.UserRepository;
import com.dindeMarket.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_CLIENT')")
@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserRepository userRepository;

//    @PostMapping("/create/with/subcategory")
//    public CategoryEntity createWithSubcategory(@RequestBody CategoryEntity category) {
//        return categoryService.saveCategoryWithSubcategory(category);
//    }

    @GetMapping
    public List<CategoryResponse> getAllCategories(@AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userRepository.findByUsername(userDetails.getUsername()).get();
        return categoryService.getAllCategories(user);
    }

    @GetMapping("/{id}")
    public Optional<CategoryResponse> getCategory(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    public CategoryResponse createCategory(@RequestBody CategoryRequest categoryRequest,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userRepository.findByUsername(userDetails.getUsername()).get();
        return categoryService.saveCategory(categoryRequest,user);
    }

    @PutMapping("/{id}")
    public CategoryResponse updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        return categoryService.updateCategory(id, categoryRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

     @DeleteMapping
    public ResponseEntity<Void> deleteCategories(@RequestBody List<Long> ids) {
        categoryService.deleteCategoriesByIds(ids);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/favorites")
    public List<CategoryResponse> getFavoriteCategories(@AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userRepository.findByUsername(userDetails.getUsername()).get();
        return categoryService.getFavoriteCategories(user);
    }
}
