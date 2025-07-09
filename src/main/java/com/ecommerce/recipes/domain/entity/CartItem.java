package com.ecommerce.recipes.domain.entity;

import com.ecommerce.recipes.domain.enums.CartItemSourceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cart_items")
@Getter @Setter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cart_id", nullable = false)
    private Long cartId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "source_id")
    private Long sourceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type")
    private CartItemSourceType sourceType;
}

