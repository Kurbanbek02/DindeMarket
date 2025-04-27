package com.dindeMarket.db.repository;

import com.dindeMarket.db.entity.OrderStatusInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusInfoRepository extends JpaRepository<OrderStatusInfo,Long> {
}
