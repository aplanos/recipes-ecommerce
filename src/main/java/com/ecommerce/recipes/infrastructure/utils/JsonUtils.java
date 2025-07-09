package com.ecommerce.recipes.infrastructure.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Optional;

public class JsonUtils {

    public static ObjectMapper getDefaultMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String encodeToJson(Object value) throws JsonProcessingException {
        return getDefaultMapper().writeValueAsString(value);

    }

    public static <T> Optional<T> decodeFromJson(String value, Class<T> targetClass) {
        try {
            return Optional.ofNullable(getDefaultMapper().readValue(value, targetClass));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
