package com.dindeMarket.api.payload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class ProductOrderRequest {
    private Long productId;
    private Integer quantity;
}
