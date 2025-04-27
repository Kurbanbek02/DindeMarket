package com.dindeMarket.api.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegionResponse {
    private Long id;
    private String name;
    private Double priceDelivery;
}