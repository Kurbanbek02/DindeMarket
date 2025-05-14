package com.dindeMarket.api.controller;

import com.dindeMarket.api.payload.ProductRequest;
import com.dindeMarket.api.payload.ProductResponse;
import com.dindeMarket.config.s3service.AwsFileService;
import com.dindeMarket.db.entity.PhotoEntity;
import com.dindeMarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/products")
@PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_CLIENT')")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private AwsFileService awsFileService;

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Optional<ProductResponse> getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
        return productService.saveProduct(productRequest);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        return productService.updateProduct(id, productRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProducts(@RequestBody List<Long> ids) {
        productService.deleteProductsByIds(ids);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/hide")
    public ResponseEntity<Void> hideProducts(@RequestBody List<Long> ids) {
        productService.hideProductsByIds(ids,true);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/visible")
    public ResponseEntity<Void> visibleProducts(@RequestBody List<Long> ids) {
        productService.hideProductsByIds(ids,false);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{productId}/photos")
    public ResponseEntity<String> addPhotoToProduct(@PathVariable Long productId, @RequestParam("file") MultipartFile file) {
        String photoUrl = awsFileService.uploadFile(file);
        productService.addPhotoToProduct(productId, photoUrl);
        return new ResponseEntity<>(photoUrl, HttpStatus.OK);
    }

    @GetMapping("/{productId}/photos")
    public ResponseEntity<Set<PhotoEntity>> getPhotosByProduct(@PathVariable Long productId) {
        Set<PhotoEntity> photoEntities = productService.getPhotosByProduct(productId);
        return new ResponseEntity<>(photoEntities, HttpStatus.OK);
    }
    @GetMapping("/subcategory/{subcategoryId}")
    public ResponseEntity<List<ProductResponse>> getProductsBySubcategoryId(@PathVariable Long subcategoryId) {
        List<ProductResponse> products = productService.getProductsBySubcategoryId(subcategoryId);
        return ResponseEntity.ok(products);
    }
}
