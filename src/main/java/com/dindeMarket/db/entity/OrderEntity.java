package com.dindeMarket.db.entity;

import javax.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true, foreignKey = @ForeignKey(name = "fk_user_order", foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL"))
    private UserEntity user;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProductEntity> products = new ArrayList<>();


    private Double totalAmount;

    private Double discount;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderStatusInfo> orderStatus = new ArrayList<>(); // Связь с состоянием заказа

    private PaymentType paymentType;
    private String phoneNumber;

    private String comment;

    private String firstName;

    private String lastName;

    private String city;

    private String street;

    private String unit;

    private String entrance;

    private String floor;
}
