package com.ecommerce.recipes.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
public class CartModel {
    Long id;
    BigDecimal totalInCents;
    List<CartItemModel> items;
}
