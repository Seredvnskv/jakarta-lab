package org.com.recipes.cuisine.converter;

import jakarta.enterprise.context.Dependent;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.cuisine.Cuisine;
import org.com.recipes.cuisine.service.CuisineService;

import java.util.UUID;

@FacesConverter(value = "cuisineConverter", managed = true)
@Dependent
public class CuisineConverter implements Converter<Cuisine> {

    @Inject
    private CuisineService cuisineService;

    @Override
    public Cuisine getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return cuisineService.findCuisineById(UUID.fromString(value));
        }
        catch (IllegalArgumentException | NotFoundException e) {
            throw new ConverterException("Invalid cuisine: " + value, e);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Cuisine value) {
        return value == null ? "" : value.getId().toString();
    }
}
