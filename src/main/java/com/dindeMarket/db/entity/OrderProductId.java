package com.dindeMarket.db.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
@Setter
@Getter
@EqualsAndHashCode
public class OrderProductId implements Serializable {

    private Long orderId;
    private Long productId;

    // Конструкторы, equals, hashCode
    public OrderProductId() {}

    public OrderProductId(Long orderId, Long productId) {
        this.orderId = orderId;
        this.productId = productId;
    }
}
