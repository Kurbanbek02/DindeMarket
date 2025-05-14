package com.dindeMarket.api.payload;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private String photo;
    private Boolean favorites;
    private LocalDateTime creationDate;
    private List<SubcategoryResponse> subcategories; // Используем SubcategoryResponse для подкатегорий
}
