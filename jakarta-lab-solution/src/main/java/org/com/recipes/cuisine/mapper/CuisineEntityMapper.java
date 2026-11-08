package org.com.recipes.cuisine.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import org.com.recipes.core.collection.UICollectionRepresentation;
import org.com.recipes.core.mapper.EntityMapper;
import org.com.recipes.cuisine.dto.CuisineDto;
import org.com.recipes.cuisine.entity.Continent;
import org.com.recipes.cuisine.entity.Cuisine;
import org.com.recipes.cuisine.entity.id.CuisineId;
import org.com.recipes.recipe.mapper.RecipeEntityMapper;

import java.util.Collection;

@ApplicationScoped
@NoArgsConstructor
public class CuisineEntityMapper implements EntityMapper<Cuisine, CuisineDto> {

    private RecipeEntityMapper recipeEntityMapper;

    @Inject
    public CuisineEntityMapper(RecipeEntityMapper recipeEntityMapper) {
        this.recipeEntityMapper = recipeEntityMapper;
    }

    @Override
    public Collection<CuisineDto> toDtos(Collection<Cuisine> entities) {
        return entities.stream().map(this::toDto).toList();
    }

    @Override
    public CuisineDto toDto(Cuisine entity) {
        return CuisineDto.builder()
                .id(entity.getId().id().toString())
                .continent(entity.getContinent().toString())
                .name(entity.getName())
                .spicy(entity.isSpicy())
                .recipes(recipeEntityMapper.toCollectionDtos(entity.getRecipes()))
                .build();
    }

    @Override
    public Cuisine toEntity(CuisineDto dto) {
        return Cuisine.builder()
                .id(CuisineId.of(dto.id()))
                .continent(Continent.fromString(dto.continent()))
                .name(dto.name())
                .spicy(dto.spicy())
                .build();
    }

    public UICollectionRepresentation<CuisineDto> toUICollectionDtos(Collection<CuisineDto> dtos) {
        return new UICollectionRepresentation<>(dtos);
    }
}
