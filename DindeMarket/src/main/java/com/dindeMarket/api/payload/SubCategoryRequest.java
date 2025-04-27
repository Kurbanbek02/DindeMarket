package com.dindeMarket.api.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubCategoryRequest {
    private String name;
    private Long categoryId;
}
