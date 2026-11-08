package org.com.recipes.cuisine.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.com.recipes.core.collection.CollectionRepresentation;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.cuisine.dto.CuisineDto;
import org.com.recipes.cuisine.entity.Continent;
import org.com.recipes.cuisine.entity.Cuisine;
import org.com.recipes.cuisine.entity.id.CuisineId;
import org.com.recipes.cuisine.mapper.CuisineEntityMapper;
import org.com.recipes.cuisine.repository.CuisineRepository;

import java.util.Collection;

@ApplicationScoped
@NoArgsConstructor
public class CuisineService {
    private CuisineRepository cuisineRepository;
    private CuisineEntityMapper cuisineEntityMapper;

    @Inject
    public CuisineService(CuisineRepository cuisineRepository, CuisineEntityMapper cuisineEntityMapper) {
        this.cuisineRepository = cuisineRepository;
        this.cuisineEntityMapper =  cuisineEntityMapper;
    }

    public CollectionRepresentation<CuisineDto> getCuisines() {
        Collection<Cuisine> cuisines = cuisineRepository.findAll();
        return CollectionRepresentation.of(cuisineEntityMapper.toDtos(cuisines));
    }

    public CuisineDto getCuisine(CuisineId id) {
        return cuisineRepository.findById(id).map(cuisineEntityMapper::toDto).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public CuisineDto createCuisine(CuisineDto dto) {
        Cuisine cuisine = cuisineEntityMapper.toEntity(dto);
        Cuisine saved = cuisineRepository.save(cuisine);
        return cuisineEntityMapper.toDto(saved);
    }

    @Transactional
    public CuisineDto updateCuisine(CuisineId id, CuisineDto dto) {
        Cuisine existing = cuisineRepository.findById(id).orElseThrow(NotFoundException::new);
        existing.setContinent(Continent.fromString(dto.continent()));
        existing.setName(dto.name());
        existing.setSpicy(dto.spicy());
        Cuisine saved = cuisineRepository.save(existing);
        return cuisineEntityMapper.toDto(saved);
    }

    @Transactional
    public void deleteCuisine(CuisineId id) {
        Cuisine existing = cuisineRepository.findById(id).orElseThrow(NotFoundException::new);
        cuisineRepository.delete(existing);
    }
}
