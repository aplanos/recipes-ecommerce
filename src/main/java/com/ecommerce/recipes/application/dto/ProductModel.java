package com.ecommerce.recipes.application.dto;

import com.ecommerce.recipes.domain.entity.Product;
import com.ecommerce.recipes.domain.enums.ProductUnit;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class ProductModel {
    Long id;
    String name;
    BigDecimal priceInCents;
    private Integer unitQuantity;
    private ProductUnit unitLabel;

    public ProductModel(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.priceInCents = product.getPriceInCents();
        this.unitQuantity = product.getUnitQuantity();
        this.unitLabel = product.getUnitLabel();
    }
}
