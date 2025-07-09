package com.ecommerce.recipes.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddRecipeRequestModel {
    @NotNull
    private Long recipeId;
    private Integer quantity = 1;
}
