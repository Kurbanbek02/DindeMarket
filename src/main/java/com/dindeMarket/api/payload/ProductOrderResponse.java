package com.dindeMarket.api.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProductOrderResponse {
    private ProductResponse product;
    private Integer quantity;
}
