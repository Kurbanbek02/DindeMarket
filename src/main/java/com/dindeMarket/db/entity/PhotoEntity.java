package com.dindeMarket.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class PhotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "url", nullable = false)
    private String url; // Ссылка на фото в S3

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    // Getters and setters
}
