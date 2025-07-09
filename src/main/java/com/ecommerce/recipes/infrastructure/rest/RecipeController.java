package com.ecommerce.recipes.infrastructure.rest;

import com.ecommerce.recipes.application.api.RecipeControllerApi;
import com.ecommerce.recipes.application.dto.RecipeModel;
import com.ecommerce.recipes.application.service.RecipeService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecipeController implements RecipeControllerApi {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Override
    public PagedModel<RecipeModel> getRecipes(
            @RequestParam(value = "page") @Min(0) int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") @Min(1) @Max(100) int pageSize
    ) {
        return new PagedModel<>(recipeService.findAll(page, pageSize));
    }
}
