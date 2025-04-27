package com.dindeMarket.api.payload;

import com.dindeMarket.db.entity.OrderStatus;
import com.dindeMarket.db.entity.OrderStatusInfo;
import com.dindeMarket.db.entity.PaymentType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private List<ProductOrderResponse> products = new ArrayList<>();
    private Double totalAmount;
    private Double discount;
    private Double priceDelivery;
    private List<OrderStatusInfo> orderStatus = new ArrayList<>();
    private String  phoneNumber;
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
