package com.ecommerce.recipes.application.service;

import com.ecommerce.recipes.application.dto.AddRecipeRequestModel;
import com.ecommerce.recipes.application.dto.CartModel;

public interface CartService {
    CartModel findById(long id);
    void addRecipe(long cartId, AddRecipeRequestModel recipeModel);
    void deleteRecipe(long cartId, long recipeId);
}
