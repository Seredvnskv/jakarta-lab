package org.com.recipes.cuisine.view;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.com.recipes.cuisine.dto.CuisineDto;
import org.com.recipes.cuisine.entity.Continent;
import org.com.recipes.cuisine.entity.id.CuisineId;
import org.com.recipes.cuisine.service.CuisineService;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Named
@ViewScoped
@NoArgsConstructor
public class CuisineFormView implements Serializable {
    private CuisineService cuisineService;

    @Inject
    public CuisineFormView(CuisineService cuisineService) {
        this.cuisineService = cuisineService;
    }

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String continent;

    @Getter
    @Setter
    private boolean spicy;

    @Getter
    private final List<Continent> continentOptions = List.of(Continent.values());

    public void init() {
        if (isEditing()) {
            CuisineDto cuisine = cuisineService.getCuisine(CuisineId.of(id));
            name = cuisine.name();
            continent = cuisine.continent();
            spicy = cuisine.spicy();
        }
    }

    public boolean isEditing() {
        return id != null && !id.isBlank();
    }

    public String save() {
        CuisineDto dto = CuisineDto.builder()
                .id(isEditing() ? id : UUID.randomUUID().toString())
                .name(name)
                .continent(continent)
                .spicy(spicy)
                .build();
        CuisineDto saved = isEditing()
                ? cuisineService.updateCuisine(CuisineId.of(id), dto)
                : cuisineService.createCuisine(dto);
        return "/cuisines/cuisine-details-view?faces-redirect=true&id=" + saved.id();
    }

    public String cancel() {
        return isEditing()
                ? "/cuisines/cuisine-details-view?faces-redirect=true&id=" + id
                : "/cuisines/cuisine-list-view?faces-redirect=true";
    }
}
