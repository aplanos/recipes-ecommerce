package com.ecommerce.recipes.application.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.ecommerce.recipes.application.dto.AddRecipeRequestModel;
import com.ecommerce.recipes.application.dto.CartModel;
import com.ecommerce.recipes.application.dto.RecipeProductModel;
import com.ecommerce.recipes.application.service.RecipeService;
import com.ecommerce.recipes.domain.entity.*;
import com.ecommerce.recipes.domain.enums.CartItemSourceType;
import com.ecommerce.recipes.infrastructure.repository.CartItemRepository;
import com.ecommerce.recipes.infrastructure.repository.CartRepository;
import com.ecommerce.recipes.infrastructure.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class CartServiceImplTest {

    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;
    private RecipeService recipeService;
    private CartServiceImpl cartService;

    @BeforeEach
    public void setup() {
        cartRepository = mock(CartRepository.class);
        productRepository = mock(ProductRepository.class);
        cartItemRepository = mock(CartItemRepository.class);
        recipeService = mock(RecipeService.class);
        cartService = new CartServiceImpl(cartRepository, cartItemRepository, recipeService,productRepository);
    }

    @Test
    public void testFindById_found() {
        var cart = new Cart();
        cart.setId(1L);
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        CartModel result = cartService.findById(1L);

        assertNotNull(result);
        assertEquals(cart.getId(), result.getId());
    }

    @Test
    public void testFindById_notFound() {
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            cartService.findById(1L);
        });

        assertEquals("Cart not found", ex.getMessage());
    }

    @Test
    public void testAddRecipe_newCartItem() {
        long cartId = 1L;
        long recipeId = 10L;
        int quantity = 2;

        var cart = new Cart();
        cart.setId(1L);
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        AddRecipeRequestModel recipeModel = new AddRecipeRequestModel();
        recipeModel.setRecipeId(recipeId);
        recipeModel.setQuantity(quantity);

        Product product = new Product();
        product.setId(100L);
        product.setPriceInCents(BigDecimal.TEN);
        product.setUnitQuantity(1000);

        RecipeProduct recipeProduct = new RecipeProduct();
        recipeProduct.setId(1L);
        recipeProduct.setRecipeId(recipeId);
        recipeProduct.setProductId(product.getId());

        RecipeProductModel ingredient = new RecipeProductModel(recipeProduct, product);
        ingredient.setAmount(new BigDecimal("150")); // 150 units per ingredient

        when(recipeService.getRecipeProducts(recipeId)).thenReturn(List.of(ingredient));
        when(cartItemRepository.findByCartIdAndProductIdAndSourceTypeAndSourceId(
                eq(cartId),
                eq(product.getId()),
                eq(CartItemSourceType.RECIPE),
                eq(recipeId)
        )).thenReturn(Optional.empty());

        ArgumentCaptor<CartItem> captor = ArgumentCaptor.forClass(CartItem.class);
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(i -> i.getArgument(0));

        cartService.addRecipe(cartId, recipeModel);

        verify(cartItemRepository, times(1)).save(captor.capture());
        CartItem savedItem = captor.getValue();

        // Needed amount = 150 * 2 = 300
        // units to add = ceil(300 / 1000) = 1
        assertEquals(cartId, savedItem.getCartId());
        assertEquals(product.getId(), savedItem.getProductId());
        assertEquals(CartItemSourceType.RECIPE, savedItem.getSourceType());
        assertEquals(recipeId, savedItem.getSourceId());
        assertEquals(1, savedItem.getQuantity());
    }

    @Test
    public void testAddRecipe_existingCartItem() {
        long cartId = 1L;
        long recipeId = 10L;

        var cart = new Cart();
        cart.setId(1L);
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        AddRecipeRequestModel recipeModel = new AddRecipeRequestModel();
        recipeModel.setRecipeId(recipeId);
        recipeModel.setQuantity(1);

        Product product = new Product();
        product.setId(100L);
        product.setPriceInCents(BigDecimal.TEN);
        product.setUnitQuantity(100);

        RecipeProduct recipeProduct = new RecipeProduct();
        recipeProduct.setId(1L);
        recipeProduct.setRecipeId(recipeId);
        recipeProduct.setProductId(product.getId());

        RecipeProductModel ingredient = new RecipeProductModel(recipeProduct, product);
        ingredient.setAmount(new BigDecimal("50")); // 50 units per ingredient

        CartItem existingItem = new CartItem();
        existingItem.setId(5L);
        existingItem.setCartId(cartId);
        existingItem.setProductId(product.getId());
        existingItem.setSourceType(CartItemSourceType.RECIPE);
        existingItem.setSourceId(recipeId);
        existingItem.setQuantity(2);

        when(recipeService.getRecipeProducts(recipeId)).thenReturn(List.of(ingredient));
        when(cartItemRepository.findByCartIdAndProductIdAndSourceTypeAndSourceId(
                eq(cartId), eq(product.getId()), eq(CartItemSourceType.RECIPE), eq(recipeId)))
                .thenReturn(Optional.of(existingItem));
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(i -> i.getArgument(0));

        cartService.addRecipe(cartId, recipeModel);

        verify(cartItemRepository).save(existingItem);

        // Needed amount = 50 * 1 = 50
        // units to add = ceil(50 / 100) = 1
        // quantity updated from 2 to 3
        assertEquals(3, existingItem.getQuantity());
    }

    @Test
    public void testDeleteRecipe_foundItems() {
        long cartId = 1L;
        long recipeId = 10L;

        var cart = new Cart();
        cart.setId(1L);
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        CartItem item1 = new CartItem();
        item1.setId(1L);
        item1.setQuantity(1);
        item1.setProductId(1L);

        Product p = new Product();
        p.setPriceInCents(BigDecimal.TEN);

        when(productRepository.findById(1L)).thenReturn(Optional.of(p));

        CartItem item2 = new CartItem();
        item2.setId(2L);
        item2.setQuantity(1);
        item2.setProductId(2L);

        when(productRepository.findById(2L)).thenReturn(Optional.of(p));

        when(cartItemRepository.findByCartIdAndSourceTypeAndSourceId(cartId, CartItemSourceType.RECIPE, recipeId))
                .thenReturn(List.of(item1, item2));

        cartService.deleteRecipe(cartId, recipeId);

        verify(cartItemRepository).deleteAll(List.of(item1, item2));
    }

    @Test
    public void testDeleteRecipe_noItems() {
        long cartId = 1L;
        long recipeId = 10L;

        var cart = new Cart();
        cart.setId(1L);
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        when(cartItemRepository.findByCartIdAndSourceTypeAndSourceId(cartId, CartItemSourceType.RECIPE, recipeId))
                .thenReturn(List.of());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            cartService.deleteRecipe(cartId, recipeId);
        });

        assertEquals("No recipe items found in cart", ex.getMessage());
    }
}
