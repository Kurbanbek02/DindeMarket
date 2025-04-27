package com.dindeMarket.db.repository;

import com.dindeMarket.db.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    @Query("SELECT c FROM CategoryEntity c WHERE c.favorites = false and c.region.id = :regionId")
    List<CategoryEntity> findAllByFavorites(Long regionId);

    @Query("SELECT c FROM CategoryEntity c WHERE c.favorites = true and c.region.id = :regionId")
    List<CategoryEntity> findFavorites(Long regionId);
}
