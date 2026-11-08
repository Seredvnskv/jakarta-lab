package org.com.recipes.cuisine.converter;

import jakarta.enterprise.context.Dependent;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import org.com.recipes.cuisine.dto.CuisineDto;
import org.com.recipes.cuisine.entity.id.CuisineId;
import org.com.recipes.cuisine.service.CuisineService;

@Dependent
@FacesConverter(value = "cuisineConverter", managed = true)
public class CuisineConverter implements Converter<CuisineDto> {

    @Inject
    private CuisineService cuisineService;

    @Override
    public CuisineDto getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return cuisineService.getCuisine(CuisineId.of(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, CuisineDto value) {
        return value == null ? "" : value.id();
    }
}
