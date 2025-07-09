package com.ecommerce.recipes.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class RecipeModel {
    private Long id;
    private String name;
    private List<RecipeProductModel> ingredients;
}
