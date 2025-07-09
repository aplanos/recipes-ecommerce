package com.ecommerce.recipes.application.service.impl;

import com.ecommerce.recipes.application.dto.RecipeModel;
import com.ecommerce.recipes.application.dto.RecipeProductModel;
import com.ecommerce.recipes.application.service.RecipeService;
import com.ecommerce.recipes.domain.entity.Product;
import com.ecommerce.recipes.domain.entity.Recipe;
import com.ecommerce.recipes.domain.entity.RecipeProduct;
import com.ecommerce.recipes.infrastructure.repository.ProductRepository;
import com.ecommerce.recipes.infrastructure.repository.RecipeProductRepository;
import com.ecommerce.recipes.infrastructure.repository.RecipeRepository;
import com.ecommerce.recipes.infrastructure.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final ModelMapper modelMapper = MapperUtils.getDefaultMapper();
    private final RecipeRepository recipeRepository;
    private final RecipeProductRepository recipeProductRepository;
    private final ProductRepository productRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             RecipeProductRepository recipeProductRepository,
                             ProductRepository productRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeProductRepository = recipeProductRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Page<RecipeModel> findAll(int page, int pageSize) {
        var pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        Page<Recipe> recipePage = recipeRepository.findAll(pageRequest);

        List<RecipeModel> recipeModels = recipePage.getContent().stream().map(recipe -> {
            RecipeModel model = modelMapper.map(recipe, RecipeModel.class);
            model.setIngredients(getRecipeProducts(recipe.getId()));
            return model;
        }).toList();

        return new PageImpl<>(recipeModels, pageRequest, recipePage.getTotalElements());
    }

    @Override
    public List<RecipeProductModel> getRecipeProducts(Long recipeId) {
        List<RecipeProduct> recipeProducts = recipeProductRepository.findByRecipeId(recipeId);

        Set<Long> productIds = recipeProducts.stream()
                .map(RecipeProduct::getProductId)
                .collect(Collectors.toSet());

        List<Product> products = productRepository.findAllById(productIds);

        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        List<RecipeProductModel> recipeProductModels = new ArrayList<>();
        for (RecipeProduct rp : recipeProducts) {
            Product product = productMap.get(rp.getProductId());
            recipeProductModels.add(new RecipeProductModel(rp, product));
        }

        return recipeProductModels;
    }
}
