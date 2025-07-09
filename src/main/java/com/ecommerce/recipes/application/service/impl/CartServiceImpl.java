package com.ecommerce.recipes.application.service.impl;

import com.ecommerce.recipes.application.dto.AddRecipeRequestModel;
import com.ecommerce.recipes.application.dto.CartItemModel;
import com.ecommerce.recipes.application.dto.CartModel;
import com.ecommerce.recipes.application.service.CartService;
import com.ecommerce.recipes.application.service.RecipeService;
import com.ecommerce.recipes.domain.entity.CartItem;
import com.ecommerce.recipes.domain.enums.CartItemSourceType;
import com.ecommerce.recipes.infrastructure.repository.CartItemRepository;
import com.ecommerce.recipes.infrastructure.repository.CartRepository;
import com.ecommerce.recipes.infrastructure.repository.ProductRepository;
import com.ecommerce.recipes.infrastructure.utils.MapperUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final RecipeService recipeService;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           RecipeService recipeService,
                           ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.recipeService = recipeService;
        this.productRepository = productRepository;
    }

    @Override
    public CartModel findById(long id) {
        return cartRepository.findById(id)
                .map(cart -> {
                    CartModel cartModel = MapperUtils.mapTo(cart, CartModel.class);

                    List<CartItemModel> itemModels = cartItemRepository.findByCartId(id).stream()
                            .map(item -> MapperUtils.mapTo(item, CartItemModel.class))
                            .collect(Collectors.toList());

                    cartModel.setItems(itemModels);
                    return cartModel;
                })
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
    }

    @Override
    @Transactional
    public void addRecipe(long cartId, AddRecipeRequestModel recipeModel) {

        final var cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart with id " + cartId + " does not exist"));

        var totalInCents = BigDecimal.ZERO;

        final var recipeId = recipeModel.getRecipeId();
        final int times = Optional.ofNullable(recipeModel.getQuantity()).orElse(1);

        final var ingredients = recipeService.getRecipeProducts(recipeId);

        for (final var ingredient : ingredients) {
            final var product = ingredient.getProduct();

            if (product.getUnitLabel() != ingredient.getAmountUnit()) {
               throw new RuntimeException("Unit conversion not implemented yet");
            }

            int unitQty = product.getUnitQuantity();
            BigDecimal neededAmount = ingredient.getAmount()
                    .multiply(BigDecimal.valueOf(times));

            int unitsToAdd = (int) Math.ceil(neededAmount.doubleValue() / unitQty);

            final var cartItem = cartItemRepository.findByCartIdAndProductIdAndSourceTypeAndSourceId(
                    cartId,
                    product.getId(),
                    CartItemSourceType.RECIPE,
                    recipeId
            ).orElse(new CartItem());

            if (cartItem.getId() == null) {
                cartItem.setCartId(cartId);
                cartItem.setProductId(product.getId());
                cartItem.setSourceType(CartItemSourceType.RECIPE);
                cartItem.setSourceId(recipeId);
                cartItem.setQuantity(0);
            }

            cartItem.setQuantity(cartItem.getQuantity() + unitsToAdd);
            cartItemRepository.save(cartItem);

            totalInCents = totalInCents.add(product.getPriceInCents().multiply(BigDecimal.valueOf(unitsToAdd)));
        }

        if (cart.getTotalInCents() == null) {
            cart.setTotalInCents(totalInCents);
        } else {
            cart.setTotalInCents(cart.getTotalInCents().add(totalInCents));
        }

        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void deleteRecipe(long cartId, long recipeId) {
        final var cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart with id " + cartId + " does not exist"));

        final var items = cartItemRepository.findByCartIdAndSourceTypeAndSourceId(
                cartId, CartItemSourceType.RECIPE, recipeId);

        if (items.isEmpty()) {
            throw new RuntimeException("No recipe items found in cart");
        }

        // TODO: Store unit price in CartItem to ensure accurate price calculations even if product prices change later
        BigDecimal totalToSubtract = items.stream()
                .map(item -> {
                    // Assuming you can get product price from productId
                    var product = productRepository.findById(item.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found for id " + item.getProductId()));
                    return product.getPriceInCents().multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalInCents(
                Optional.ofNullable(cart.getTotalInCents())
                        .map(total -> total.subtract(totalToSubtract))
                        .orElse(BigDecimal.ZERO.subtract(totalToSubtract))  // If null, start from zero minus totalToSubtract
        );

        // Delete all recipe items
        cartItemRepository.deleteAll(items);
        cartRepository.save(cart);
    }

}
