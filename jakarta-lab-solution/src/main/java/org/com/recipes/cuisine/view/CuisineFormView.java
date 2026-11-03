package org.com.recipes.cuisine.view;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.com.recipes.cuisine.Continent;
import org.com.recipes.cuisine.Cuisine;
import org.com.recipes.cuisine.service.CuisineService;

import java.io.Serializable;
import java.util.UUID;

@Named
@ViewScoped
@Getter
@Setter
public class CuisineFormView implements Serializable {
    private final CuisineService cuisineService;

    private String name;
    private Continent continent;
    private boolean spicy;

    @Inject
    public CuisineFormView(CuisineService cuisineService) {
        this.cuisineService = cuisineService;
    }

    public Continent[] continents() {
        return Continent.values();
    }

    public String save() {
        Cuisine cuisine = Cuisine.builder()
                .id(UUID.randomUUID())
                .name(name)
                .continent(continent)
                .spicy(spicy)
                .build();
        cuisineService.createCuisine(cuisine);
        return "/cuisines/cuisines_list?faces-redirect=true";
    }

    public String cancel() {
        return "/cuisines/cuisines_list?faces-redirect=true";
    }
}
