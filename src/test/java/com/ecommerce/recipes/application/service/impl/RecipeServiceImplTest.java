package com.ecommerce.recipes.application.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.ecommerce.recipes.application.dto.RecipeProductModel;
import com.ecommerce.recipes.domain.entity.Product;
import com.ecommerce.recipes.domain.entity.RecipeProduct;
import com.ecommerce.recipes.domain.enums.ProductUnit;
import com.ecommerce.recipes.infrastructure.repository.ProductRepository;
import com.ecommerce.recipes.infrastructure.repository.RecipeProductRepository;
import com.ecommerce.recipes.infrastructure.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

public class RecipeServiceImplTest {

    private RecipeRepository recipeRepository;
    private RecipeProductRepository recipeProductRepository;
    private ProductRepository productRepository;
    private RecipeServiceImpl recipeService;

    @BeforeEach
    public void setup() {
        recipeRepository = mock(RecipeRepository.class);
        recipeProductRepository = mock(RecipeProductRepository.class);
        productRepository = mock(ProductRepository.class);

        recipeService = new RecipeServiceImpl(recipeRepository, recipeProductRepository, productRepository);
    }

    @Test
    public void testGetRecipeProducts_returnsMappedRecipeProductModels() {
        long recipeId = 5L;

        // Create RecipeProduct entities
        RecipeProduct rp1 = new RecipeProduct();
        rp1.setId(1L);
        rp1.setRecipeId(recipeId);
        rp1.setProductId(100L);
        rp1.setAmount(new java.math.BigDecimal("10.5"));
        rp1.setUsageDescription("Desc 1");
        rp1.setAmountUnit(ProductUnit.G);

        RecipeProduct rp2 = new RecipeProduct();
        rp2.setId(2L);
        rp2.setRecipeId(recipeId);
        rp2.setProductId(101L);
        rp2.setAmount(new java.math.BigDecimal("5.0"));
        rp2.setUsageDescription("Desc 2");
        rp2.setAmountUnit(ProductUnit.PCS);

        List<RecipeProduct> recipeProducts = List.of(rp1, rp2);

        when(recipeProductRepository.findByRecipeId(recipeId)).thenReturn(recipeProducts);

        // Create corresponding products
        Product p1 = new Product();
        p1.setId(100L);
        p1.setName("Flour");
        p1.setPriceInCents(new java.math.BigDecimal("500"));
        p1.setUnitQuantity(1000);
        p1.setUnitLabel(ProductUnit.G);

        Product p2 = new Product();
        p2.setId(101L);
        p2.setName("Eggs");
        p2.setPriceInCents(new java.math.BigDecimal("800"));
        p2.setUnitQuantity(12);
        p2.setUnitLabel(ProductUnit.PCS);

        when(productRepository.findAllById(ArgumentMatchers.anySet()))
                .thenReturn(List.of(p1, p2));

        List<RecipeProductModel> result = recipeService.getRecipeProducts(recipeId);

        assertNotNull(result);
        assertEquals(2, result.size());

        // Check that each RecipeProductModel has matching product and recipeProduct
        assertEquals(recipeProducts.get(0).getId(), result.get(0).getId());
        assertEquals(p1.getId(), result.get(0).getProduct().getId());

        assertEquals(recipeProducts.get(1).getId(), result.get(1).getId());
        assertEquals(p2.getId(), result.get(1).getProduct().getId());

        verify(recipeProductRepository).findByRecipeId(recipeId);
        verify(productRepository).findAllById(Set.of(100L, 101L));
    }
}