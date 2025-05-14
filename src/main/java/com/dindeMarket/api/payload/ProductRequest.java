package com.dindeMarket.api.payload;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private String description;
    private String sort;
    private Set<String> photos;
    private Double price;
    private LocalDateTime releaseDate;
    private Long count;
    private Double discount;
    private Long subcategoryId;
}
