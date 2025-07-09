package com.ecommerce.recipes.application.service;

import com.ecommerce.recipes.application.dto.RecipeModel;
import com.ecommerce.recipes.application.dto.RecipeProductModel;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RecipeService {
    Page<RecipeModel> findAll(int page, int pageSize);
    List<RecipeProductModel> getRecipeProducts(Long recipeId);
}
