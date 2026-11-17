package org.com.recipes.cuisine.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.com.recipes.core.collection.CollectionRepresentation;
import org.com.recipes.core.exception.AccessForbiddenException;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.core.exception.UnauthorizedAccessException;
import org.com.recipes.cuisine.dto.CuisineDto;
import org.com.recipes.cuisine.entity.Continent;
import org.com.recipes.cuisine.entity.Cuisine;
import org.com.recipes.cuisine.entity.id.CuisineId;
import org.com.recipes.cuisine.mapper.CuisineEntityMapper;
import org.com.recipes.cuisine.repository.CuisineRepository;
import org.com.recipes.recipe.dto.CollectionRecipeDto;
import org.com.recipes.recipe.mapper.RecipeEntityMapper;
import org.com.recipes.recipe.repository.RecipeRepository;
import org.com.recipes.user.entity.Role;
import org.com.recipes.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@ApplicationScoped
@NoArgsConstructor
public class CuisineService {
    private CuisineRepository cuisineRepository;
    private CuisineEntityMapper cuisineEntityMapper;
    private RecipeRepository recipeRepository;
    private RecipeEntityMapper recipeEntityMapper;
    private UserRepository userRepository;
    private SecurityContext securityContext;

    @Inject
    public CuisineService(CuisineRepository cuisineRepository, CuisineEntityMapper cuisineEntityMapper,
                          RecipeRepository recipeRepository, RecipeEntityMapper recipeEntityMapper,
                          UserRepository userRepository, SecurityContext securityContext) {
        this.cuisineRepository = cuisineRepository;
        this.cuisineEntityMapper = cuisineEntityMapper;
        this.recipeRepository = recipeRepository;
        this.recipeEntityMapper = recipeEntityMapper;
        this.userRepository = userRepository;
        this.securityContext = securityContext;
    }

    public CollectionRepresentation<CuisineDto> getCuisines() {
        requireRole(Role.USER, Role.ADMIN);
        Collection<Cuisine> cuisines = cuisineRepository.findAll();
        return CollectionRepresentation.of(cuisines.stream().map(this::toDto).toList());
    }

    public CuisineDto getCuisine(CuisineId id) {
        requireRole(Role.USER, Role.ADMIN);
        return cuisineRepository.findById(id).map(this::toDto).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public CuisineDto createCuisine(CuisineDto dto) {
        requireRole(Role.ADMIN);
        Cuisine cuisine = cuisineEntityMapper.toEntity(dto);
        Cuisine saved = cuisineRepository.save(cuisine);
        return cuisineEntityMapper.toDto(saved);
    }

    @Transactional
    public CuisineDto updateCuisine(CuisineId id, CuisineDto dto) {
        requireRole(Role.ADMIN);
        Cuisine existing = cuisineRepository.findById(id).orElseThrow(NotFoundException::new);
        existing.setContinent(Continent.fromString(dto.continent()));
        existing.setName(dto.name());
        existing.setSpicy(dto.spicy());
        Cuisine saved = cuisineRepository.save(existing);
        return cuisineEntityMapper.toDto(saved);
    }

    @Transactional
    public void deleteCuisine(CuisineId id) {
        requireRole(Role.ADMIN);
        Cuisine existing = cuisineRepository.findById(id).orElseThrow(NotFoundException::new);
        cuisineRepository.delete(existing);
    }

    private CuisineDto toDto(Cuisine cuisine) {
        CuisineDto dto = cuisineEntityMapper.toDto(cuisine);
        if (securityContext.isCallerInRole(Role.ADMIN.name())) {
            return dto;
        }
        Collection<CollectionRecipeDto> ownRecipes = userRepository
                .findByUsername(securityContext.getCallerPrincipal().getName())
                .map(caller -> recipeEntityMapper.toCollectionDtos(recipeRepository.findAllByCuisineAndAuthor(cuisine, caller)))
                .orElse(List.of());
        return new CuisineDto(dto.id(), dto.continent(), dto.name(), dto.spicy(), ownRecipes);
    }

    private void requireRole(Role... roles) {
        if (securityContext.getCallerPrincipal() == null) {
            throw new UnauthorizedAccessException("Authentication required");
        }
        for (Role role : roles) {
            if (securityContext.isCallerInRole(role.name())) {
                return;
            }
        }
        throw new AccessForbiddenException("Insufficient role");
    }
}
