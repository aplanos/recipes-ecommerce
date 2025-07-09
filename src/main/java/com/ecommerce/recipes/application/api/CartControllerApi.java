package com.ecommerce.recipes.application.api;

import com.ecommerce.recipes.application.dto.AddRecipeRequestModel;
import com.ecommerce.recipes.application.dto.CartModel;
import com.ecommerce.recipes.application.dto.RecipeModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Carts API", description = "Operations related to cart management")
@RequestMapping("/carts")
@Validated
public interface CartControllerApi {

    @Operation(summary = "Get cart",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cart retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartModel.class))),
            })
    @GetMapping("/{id}")
    CartModel getCart(@PathVariable @Positive Long id);

    @Operation(summary = "Add a recipe to a cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe successfully added to the cart"),
            @ApiResponse(responseCode = "400", description = "Invalid cart ID provided"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @PostMapping(value = "/{id}/add_recipe")
    ResponseEntity<?> addRecipe(@PathVariable @Positive Long id,
                                @Valid @RequestBody AddRecipeRequestModel recipeModel
    );

    @Operation(summary = "Delete recipe from cart",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recipe removed successfully from cart",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartModel.class))),
            })
    @DeleteMapping("/{cartId}/recipes/{recipeId}")
    ResponseEntity<?> deleteRecipe(@PathVariable @Positive Long cartId, @PathVariable @Positive Long recipeId);
}
