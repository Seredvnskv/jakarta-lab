package org.com.recipes.cuisine.mapper;

import lombok.experimental.UtilityClass;
import org.com.recipes.cuisine.Cuisine;
import org.com.recipes.cuisine.Region;
import org.com.recipes.cuisine.dto.CreateCuisineDto;

import java.util.UUID;

@UtilityClass
public class CuisineMapper {
    public static Cuisine toEntity(CreateCuisineDto dto) {
        return Cuisine.builder()
                .id(UUID.randomUUID())
                .name(dto.name())
                .region(Region.valueOf(dto.region()))
                .spicy(dto.spicy())
                .build();
    }
}
