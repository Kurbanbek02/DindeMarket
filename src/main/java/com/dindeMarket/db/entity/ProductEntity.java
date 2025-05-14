package com.dindeMarket.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String  sort;
    private Double price;
    private LocalDateTime releaseDate;
    private Long count;
    private Double discount;

    @Column(nullable = false)
    private Boolean hidden = false; // Поле для скрытия продукта

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id", nullable = false)
    private SubcategoryEntity subcategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PhotoEntity> photos;

}
