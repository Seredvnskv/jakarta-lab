package org.com.recipes.cuisine.view;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.com.recipes.cuisine.dto.CuisineDto;
import org.com.recipes.cuisine.mapper.CuisineMapper;
import org.com.recipes.cuisine.service.CuisineService;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Named
@ViewScoped
public class CuisineList implements Serializable {

    private final CuisineService cuisineService;

    @Inject
    public CuisineList(CuisineService cuisineService) {
        this.cuisineService = cuisineService;
    }

    public List<CuisineDto> getCuisines() {
        return CuisineMapper.toDto(cuisineService.findAllCuisines());
    }

    public String deleteCuisine(CuisineDto cuisine) {
        cuisineService.deleteCuisine(UUID.fromString(cuisine.id()));
        return "cuisines_list?faces-redirect=true";
    }

    public String viewCuisine(CuisineDto cuisine) {
        return "cuisines_view?faces-redirect=true&cuisineId=" + cuisine.id();
    }

}
