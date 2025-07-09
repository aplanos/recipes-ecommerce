package com.ecommerce.recipes.application.api;

import com.ecommerce.recipes.application.dto.RecipeModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.web.PagedModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Recipes API", description = "Operations related to recipes management")
@RequestMapping("/recipes")
@Validated
public interface RecipeControllerApi {

    @Operation(summary = "Get all recipes", description = "Fetches all recipes with pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of recipes retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedModel.class))),
            })
    @GetMapping
    PagedModel<RecipeModel> getRecipes(
            @Parameter(description = "The page number to fetch") @RequestParam(value = "page") @Min(0) int page,
            @Parameter(description = "The size of each page") @RequestParam(value = "pageSize", required = false, defaultValue = "10") @Min(1) @Max(100) int pageSize
    );
}
