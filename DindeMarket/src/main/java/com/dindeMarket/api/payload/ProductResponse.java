package com.dindeMarket.api.payload;

import com.dindeMarket.db.entity.PhotoEntity;
import com.dindeMarket.db.entity.ProductEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private String sort;
    private List<PhotoResponse> photos = new ArrayList<>();
    private Double price;
    private Long subcategoryId;         // ID подкатегории
    private LocalDateTime releaseDate;
    private Long count;
    private Double discount;
    private Boolean hidden;
}