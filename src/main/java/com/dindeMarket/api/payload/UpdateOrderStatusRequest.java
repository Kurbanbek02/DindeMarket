package com.dindeMarket.api.payload;

import com.dindeMarket.db.entity.OrderStatus;
import com.dindeMarket.db.entity.OrderStatusInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateOrderStatusRequest {
    private List<Long> orderIds; // Список ID заказов, которые нужно обновить
    private OrderStatusInfoRequest newStatus; // Новый статус
}
