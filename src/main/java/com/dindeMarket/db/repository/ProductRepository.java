package com.dindeMarket.db.repository;

import com.dindeMarket.db.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByIdIn(List<Long> ids);
    @Query("SELECT p FROM ProductEntity p WHERE p.subcategory.id = :subcategoryId")
    List<ProductEntity> findProductsBySubcategoryId(@Param("subcategoryId") Long subcategoryId);
    List<ProductEntity> findByNameContainingIgnoreCase(String name);
}
