package com.dindeMarket.service;

import com.dindeMarket.api.payload.PhotoResponse;
import com.dindeMarket.api.payload.ProductRequest;
import com.dindeMarket.api.payload.ProductResponse;
import com.dindeMarket.api.payload.SubcategoryResponse;
import com.dindeMarket.db.entity.OrderEntity;
import com.dindeMarket.db.entity.PhotoEntity;
import com.dindeMarket.db.entity.ProductEntity;
import com.dindeMarket.db.entity.SubcategoryEntity;
import com.dindeMarket.db.repository.OrderRepository;
import com.dindeMarket.db.repository.PhotoRepository;
import com.dindeMarket.db.repository.ProductRepository;
import com.dindeMarket.db.repository.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PhotoRepository photoRepository;
    private final PhotoService photoService;
    @Autowired
    private SubcategoryRepository subcategoryRepository;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Optional<ProductResponse> getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToResponse);
    }

    public ProductResponse saveProduct(ProductRequest productRequest) {
        ProductEntity product = new ProductEntity();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setSort(productRequest.getSort());
        product.setPrice(productRequest.getPrice());
        product.setReleaseDate(productRequest.getReleaseDate());
        product.setDiscount(productRequest.getDiscount());
        product.setCount(productRequest.getCount());

        SubcategoryEntity subcategory = subcategoryRepository.findById(productRequest.getSubcategoryId())
                .orElseThrow(() -> new RuntimeException("Subcategory not found"));
        product.setSubcategory(subcategory);

        // Сохраняем фотографии
        if (productRequest.getPhotos() != null) {
            Set<PhotoEntity> photos = productRequest.getPhotos().stream()
                    .map(photoUrl -> {
                        PhotoEntity photo = new PhotoEntity();
                        photo.setUrl(photoUrl);
                        photo.setProduct(product);
                        return photo;
                    })
                    .collect(Collectors.toSet());
            product.setPhotos(photos);
        }

        ProductEntity savedProduct = productRepository.save(product);
        return convertToResponse(savedProduct);
    }

    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setSort(productRequest.getSort());
        product.setPrice(productRequest.getPrice());
        product.setReleaseDate(productRequest.getReleaseDate());
        product.setDiscount(productRequest.getDiscount());
        product.setCount(productRequest.getCount());

        SubcategoryEntity subcategory = subcategoryRepository.findById(productRequest.getSubcategoryId())
                .orElseThrow(() -> new RuntimeException("Subcategory not found"));
        product.setSubcategory(subcategory);

        // Обновляем фотографии
        if (productRequest.getPhotos() != null) {
            // Очищаем текущую коллекцию фотографий
            product.getPhotos().clear();

            // Добавляем новые фотографии
            Set<PhotoEntity> photos = productRequest.getPhotos().stream()
                    .map(photoUrl -> {
                        PhotoEntity photo = new PhotoEntity();
                        photo.setUrl(photoUrl);
                        photo.setProduct(product);
                        return photo;
                    })
                    .collect(Collectors.toSet());

            product.getPhotos().addAll(photos);
        }

        ProductEntity updatedProduct = productRepository.save(product);

        return convertToResponse(updatedProduct);
    }


    public void deleteProduct(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
//
//        // Удаляем связи с продуктами в заказах
//        for (OrderEntity order : orderRepository.findByProductsContaining(product)) {
//            order.getProducts().remove(product);
//        }

        // Теперь можно удалять продукт
        productRepository.delete(product);
    }

    @Transactional
    public void deleteProductsByIds(List<Long> ids) {
        for (Long id : ids) {
            deleteProduct(id);
        }
    }

    @Transactional
    public void hideProductsByIds(List<Long> ids,Boolean hidden) {
        List<ProductEntity> products = productRepository.findAllById(ids);
        for (ProductEntity product : products) {
            product.setHidden(hidden); // Скрыть продукт
        }
        productRepository.saveAll(products);
    }



    public void addPhotoToProduct(Long productId, String photoUrl) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setUrl(photoUrl);
        photoEntity.setProduct(product);
        photoRepository.save(photoEntity);
    }

    public Set<PhotoEntity> getPhotosByProduct(Long productId) {
        return photoRepository.findByProductId(productId);
    }

    public List<ProductResponse> getProductsBySubcategoryId(Long subcategoryId) {
        List<ProductEntity> products = productRepository.findProductsBySubcategoryId(subcategoryId);

        return products.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    public ProductResponse convertToResponse(ProductEntity entity) {
        ProductResponse response = new ProductResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setSort(entity.getSort());
        List<PhotoResponse> photos = new ArrayList<>();
        for (PhotoEntity photo : entity.getPhotos()) {
            photos.add(photoService.convertToResponse(photo));
        }
        response.setPhotos(photos);
        response.setPrice(entity.getPrice());
        response.setHidden(entity.getHidden());
        response.setReleaseDate(entity.getReleaseDate());
        response.setDiscount(entity.getDiscount());
        response.setCount(entity.getCount());
        response.setSubcategoryId(entity.getSubcategory().getId()); // Предполагая, что Subcategory ID хранится в response
        return response;
    }
}
