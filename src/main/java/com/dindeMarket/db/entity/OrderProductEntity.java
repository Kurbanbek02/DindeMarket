package com.dindeMarket.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class OrderProductEntity {

    @EmbeddedId
    private OrderProductId id = new OrderProductId(); // Композитный ключ

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    private ProductEntity product;

    private Integer quantity;  // Количество продукта в заказе
}
