package com.ecommerce.recipes.infrastructure.rest;

import com.ecommerce.recipes.application.dto.AddRecipeRequestModel;
import com.ecommerce.recipes.application.dto.CartModel;
import com.ecommerce.recipes.application.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {
    @ClassRule
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11.1")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;

    @InjectMocks
    private CartController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddRecipe() throws Exception {
        Long cartId = 1L;
        AddRecipeRequestModel request = new AddRecipeRequestModel();
        request.setRecipeId(100L);
        request.setQuantity(2);

        // Mock service behavior: do nothing (void method)
        doNothing().when(cartService).addRecipe(eq(cartId), any(AddRecipeRequestModel.class));

        mockMvc.perform(post("/carts/{id}/add_recipe", cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isNoContent());

        verify(cartService, times(1)).addRecipe(eq(cartId), any(AddRecipeRequestModel.class));
    }

    @Test
    public void testGetCart() throws Exception {
        Long cartId = 1L;

        CartModel cartModel = new CartModel();
        cartModel.setId(cartId);

        when(cartService.findById(cartId)).thenReturn(cartModel);

        mockMvc.perform(get("/carts/{id}", cartId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartId));

        verify(cartService, times(1)).findById(cartId);
    }

    @Test
    public void testDeleteRecipe() throws Exception {
        long cartId = 1L;
        long recipeId = 100L;

        doNothing().when(cartService).deleteRecipe(cartId, recipeId);

        mockMvc.perform(delete("/carts/{cartId}/recipes/{recipeId}", cartId, recipeId))
                .andExpect(status().isNoContent());

        verify(cartService, times(1)).deleteRecipe(cartId, recipeId);
    }

    // Utility to convert object to JSON string using Jackson
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper()
                    .findAndRegisterModules() // to handle Java 8 date/time if needed
                    .writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
