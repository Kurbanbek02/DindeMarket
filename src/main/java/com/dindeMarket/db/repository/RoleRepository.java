package com.dindeMarket.db.repository;

import com.dindeMarket.db.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity,Long> {
    boolean existsByName(String name);
    Optional<RoleEntity> findByName(String name);
}
