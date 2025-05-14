package com.dindeMarket.db.repository;

import com.dindeMarket.db.entity.SubcategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubcategoryRepository extends JpaRepository<SubcategoryEntity, Long> {
    List<SubcategoryEntity> findByCategoryId(Long categoryId);
}
