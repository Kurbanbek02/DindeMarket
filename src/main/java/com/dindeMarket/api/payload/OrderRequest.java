package com.dindeMarket.api.payload;

import com.dindeMarket.db.entity.OrderStatus;
import com.dindeMarket.db.entity.OrderStatusInfo;
import com.dindeMarket.db.entity.PaymentType;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private List<ProductOrderRequest> products; // Список продуктов и их количества
    private Double totalAmount;
    private Double discount;
    private OrderStatusInfo orderStatus;
    private String phoneNumber;
    private String comment;
    private String firstName;
    private String lastName;
    private String city;
    private String street;
    private String unit;
    private String entrance;
    private String floor;
    private PaymentType paymentType;
}
