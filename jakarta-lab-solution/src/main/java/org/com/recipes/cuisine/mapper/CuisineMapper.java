package org.com.recipes.cuisine.mapper;

import lombok.experimental.UtilityClass;
import org.com.recipes.cuisine.Continent;
import org.com.recipes.cuisine.Cuisine;
import org.com.recipes.cuisine.dto.CreateCuisineDto;
import org.com.recipes.cuisine.dto.CuisineDto;

import java.util.List;
import java.util.UUID;

@UtilityClass
public class CuisineMapper {
    public static Cuisine toEntity(CreateCuisineDto dto) {
        return Cuisine.builder()
                .id(UUID.randomUUID())
                .name(dto.name())
                .continent(Continent.valueOf(dto.continent()))
                .spicy(dto.spicy())
                .build();
    }

    public static CuisineDto toDto(Cuisine entity) {
        return new CuisineDto(entity.getId().toString(), entity.getName(), entity.getContinent().toString(), entity.isSpicy());
    }

    public static List<CuisineDto> toDto(List<Cuisine> entities) {
        return entities.stream().map(CuisineMapper::toDto).toList();
    }
}
