package org.com.recipes.cuisine.view;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.com.recipes.core.collection.UICollectionRepresentation;
import org.com.recipes.cuisine.dto.CuisineDto;
import org.com.recipes.cuisine.entity.id.CuisineId;
import org.com.recipes.cuisine.mapper.CuisineEntityMapper;
import org.com.recipes.cuisine.service.CuisineService;

import java.io.Serializable;

@Named
@ViewScoped
@NoArgsConstructor
public class CuisineListView implements Serializable {
    private CuisineService cuisineService;
    private CuisineEntityMapper cuisineEntityMapper;

    @Getter
    private UICollectionRepresentation<CuisineDto> cuisines;

    @Inject
    public CuisineListView(CuisineService cuisineService, CuisineEntityMapper cuisineEntityMapper) {
        this.cuisineService = cuisineService;
        this.cuisineEntityMapper = cuisineEntityMapper;
    }

    public void init() {
        cuisines = cuisineEntityMapper.toUICollectionDtos(cuisineService.getCuisines().items());
    }

    public void deleteCuisine(String id) {
        cuisineService.deleteCuisine(CuisineId.of(id));
        init();
    }

    public String viewCuisine(String id) {
        return "/cuisines/cuisine-details-view?faces-redirect=true&id=" + id;
    }
}
