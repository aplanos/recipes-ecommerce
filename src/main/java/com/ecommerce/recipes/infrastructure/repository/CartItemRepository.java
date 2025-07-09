package com.ecommerce.recipes.infrastructure.repository;

import com.ecommerce.recipes.domain.entity.CartItem;
import com.ecommerce.recipes.domain.enums.CartItemSourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartIdAndProductIdAndSourceTypeAndSourceId(
            Long cartId,
            Long productId,
            CartItemSourceType sourceType,
            Long sourceId
    );

    List<CartItem> findByCartIdAndSourceTypeAndSourceId(
            Long cartId,
            CartItemSourceType sourceType,
            Long sourceId
    );

    List<CartItem> findByCartId(
            Long cartId
    );
}

