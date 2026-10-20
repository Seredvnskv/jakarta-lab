package org.com.recipes.cuisine.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.com.recipes.core.exception.InvalidDataException;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.cuisine.Cuisine;
import org.com.recipes.cuisine.repository.CuisineRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CuisineService {

    private CuisineRepository cuisineRepository;

    protected CuisineService() {}

    @Inject
    public CuisineService(CuisineRepository cuisineRepository) {
        this.cuisineRepository = cuisineRepository;
    }

    public List<Cuisine> findAllCuisines() {
        return cuisineRepository.findAll();
    }

    public Cuisine findCuisineById(UUID id) throws NotFoundException {
        return cuisineRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void createCuisine(Cuisine cuisine) throws InvalidDataException {
        cuisineRepository.findById(cuisine.getId()).ifPresent(_ -> {
            throw new InvalidDataException("Cuisine already exists");
        });
        cuisineRepository.save(cuisine);
        System.out.println("Created cuisine: %s (%s)".formatted(cuisine.getName(), cuisine.getId()));
    }

    public void deleteCuisine(UUID id) throws NotFoundException {
        Cuisine cuisine = cuisineRepository.findById(id).orElseThrow(NotFoundException::new);
        cuisineRepository.delete(cuisine);
    }
}