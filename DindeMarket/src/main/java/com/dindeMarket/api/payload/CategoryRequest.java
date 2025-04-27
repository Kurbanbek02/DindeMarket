package com.dindeMarket.api.payload;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CategoryRequest {
    private String name;
    private String photo;
    private Boolean favorites;
    private LocalDateTime creationDate;
    List<String> subсategories;
}
