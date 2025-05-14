package com.dindeMarket.api.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class SubcategoryResponse {
    private Long id;
    private String name;
    private List<ProductResponse> products;

}
