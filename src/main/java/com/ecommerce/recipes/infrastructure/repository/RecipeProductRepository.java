package com.ecommerce.recipes.infrastructure.repository;

import com.ecommerce.recipes.domain.entity.RecipeProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeProductRepository extends JpaRepository<RecipeProduct, Long> {
    @Query("SELECT rp FROM RecipeProduct rp WHERE rp.recipeId = :recipeId")
    List<RecipeProduct> findByRecipeId(@Param("recipeId") Long recipeId);
}

