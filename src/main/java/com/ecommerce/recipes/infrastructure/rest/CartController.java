package com.ecommerce.recipes.infrastructure.rest;

import com.ecommerce.recipes.application.api.CartControllerApi;
import com.ecommerce.recipes.application.dto.AddRecipeRequestModel;
import com.ecommerce.recipes.application.dto.CartModel;
import com.ecommerce.recipes.application.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController implements CartControllerApi {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public ResponseEntity<?> addRecipe(Long id, AddRecipeRequestModel recipeModel) {
        this.cartService.addRecipe(id, recipeModel);
        return ResponseEntity.noContent().build();
    }

    @Override
    public CartModel getCart(Long id) {
        return cartService.findById(id);
    }

    @Override
    public ResponseEntity<?> deleteRecipe(Long cartId, Long recipeId) {
        this.cartService.deleteRecipe(cartId, recipeId);
        return ResponseEntity.noContent().build();
    }
}
