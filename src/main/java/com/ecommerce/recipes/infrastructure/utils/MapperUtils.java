package com.ecommerce.recipes.infrastructure.utils;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;
import java.util.stream.Collectors;

public class MapperUtils {

    public static ModelMapper getDefaultMapper() {
        var mapper = new ModelMapper();

        mapper.getConfiguration()
                .setPropertyCondition(Conditions.isNotNull())
                .setMatchingStrategy(MatchingStrategies.STRICT);

        return mapper;
    }

    public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return mapList(getDefaultMapper(), source, targetClass);
    }

    public static <S, T> List<T> mapList(ModelMapper mapper, List<S> source, Class<T> targetClass) {

        return source
                .stream()
                .map(element -> mapper.map(element, targetClass))
                .collect(Collectors.toList());
    }

    public static <S, T> T mapTo(S source, Class<T> targetClass) {
        return getDefaultMapper().map(source, targetClass);
    }
}
