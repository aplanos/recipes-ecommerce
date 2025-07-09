package com.ecommerce.recipes.infrastructure.repository;

import com.ecommerce.recipes.domain.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
}

