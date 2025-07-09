package com.ecommerce.recipes.domain.entity;

import com.ecommerce.recipes.domain.enums.ProductUnit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "recipe_products", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"recipe_id", "product_id"})
})
@Getter @Setter
public class RecipeProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipe_id", nullable = false)
    private Long recipeId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "amount_unit", nullable = false)
    private ProductUnit amountUnit;

    @Column(name = "usage_description")
    private String usageDescription;
}

