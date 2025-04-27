package com.dindeMarket.api.payload;

import com.dindeMarket.db.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderStatusInfoRequest {
    private OrderStatus status;
    private LocalDateTime statusTime;
}
