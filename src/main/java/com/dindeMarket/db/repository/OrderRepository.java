package com.dindeMarket.db.repository;

import com.dindeMarket.db.entity.OrderEntity;
import com.dindeMarket.db.entity.OrderStatus;
import com.dindeMarket.db.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUserId(Long userId);
    List<OrderEntity> findByProductsContaining(ProductEntity product);

    @Query("SELECT o FROM OrderEntity o WHERE o.user.region.id = :regionId")
    List<OrderEntity> findOrdersByRegionId(@Param("regionId") Long regionId);
    @Query("SELECT o FROM OrderEntity o JOIN o.orderStatus osi WHERE o.user.region.id = :regionId AND osi.status = :status")
    List<OrderEntity> findOrdersByRegionAndStatus(@Param("regionId") Long regionId, @Param("status") OrderStatus status);

    @Query("SELECT o FROM OrderEntity o JOIN o.orderStatus osi WHERE o.user.region.id = :regionId AND osi.statusTime BETWEEN :startDate AND :endDate")
    List<OrderEntity> findOrdersByRegionAndOrderDate(@Param("regionId") Long regionId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT o FROM OrderEntity o WHERE o.id = :orderId AND o.user.region.id = :regionId")
    Optional<OrderEntity> findByIdAndRegion(@Param("orderId") Long orderId, @Param("regionId") Long regionId);
}
