package com.dindeMarket.api.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegionRequest {
    private String name;
    private Double priceDelivery;
}
