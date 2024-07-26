package com.bearsoft.pharmadepot.models.domain.entities;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pharmacy_order_id")
    private PharmacyOrder pharmacyOrder;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
}