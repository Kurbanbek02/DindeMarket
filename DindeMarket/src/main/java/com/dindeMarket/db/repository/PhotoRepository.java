package com.dindeMarket.db.repository;

import com.dindeMarket.db.entity.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {
    Set<PhotoEntity> findByProductId(Long productId);
}