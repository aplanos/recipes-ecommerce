package com.ecommerce.recipes.application.dto;

import com.ecommerce.recipes.domain.entity.Product;
import com.ecommerce.recipes.domain.entity.RecipeProduct;
import com.ecommerce.recipes.domain.enums.ProductUnit;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class RecipeProductModel {
    private Long id;
    private Long recipeId;
    private ProductModel product;
    private BigDecimal amount;
    private ProductUnit amountUnit;
    private String usageDescription;

    public RecipeProductModel(RecipeProduct recipeProduct, Product product) {
        this.id = recipeProduct.getId();
        this.recipeId = recipeProduct.getRecipeId();
        this.amount = recipeProduct.getAmount();
        this.amountUnit = recipeProduct.getAmountUnit();
        this.usageDescription = recipeProduct.getUsageDescription();
        this.product = new ProductModel(product);
    }
}
