package com.ecommerce.recipes.domain.entity;

import com.ecommerce.recipes.domain.enums.ProductUnit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter @Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "price_in_cents", nullable = false)
    private BigDecimal priceInCents;

    @Column(name = "unit_quantity", nullable = false)
    private Integer unitQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_label")
    private ProductUnit unitLabel;
}


