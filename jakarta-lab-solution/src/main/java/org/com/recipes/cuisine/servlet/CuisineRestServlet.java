package org.com.recipes.cuisine.servlet;

import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.com.recipes.core.dto.ListResponseDto;
import org.com.recipes.cuisine.Cuisine;
import org.com.recipes.cuisine.dto.CreateCuisineDto;
import org.com.recipes.cuisine.mapper.CuisineMapper;
import org.com.recipes.cuisine.service.CuisineService;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "CuisineRestServlet", urlPatterns = "/api/servlet/cuisines/*")
public class CuisineRestServlet extends HttpServlet {
    private final CuisineService cuisineService;
    private final Jsonb jsonb;

    @Inject
    public CuisineRestServlet(CuisineService cuisineService, Jsonb jsonb) {
        this.cuisineService = cuisineService;
        this.jsonb = jsonb;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            jsonb.toJson(new ListResponseDto<>(cuisineService.findAllCuisines()), resp.getWriter());
        }
        else {
            String id = path.substring(1);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            jsonb.toJson(cuisineService.findCuisineById(UUID.fromString(id)), resp.getWriter());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cuisine cuisine = CuisineMapper.toEntity(jsonb.fromJson(req.getInputStream(), CreateCuisineDto.class));
        cuisineService.createCuisine(cuisine);
        resp.setContentType("application/json");
        resp.setHeader("Location", req.getServletPath() + "/" + cuisine.getId());
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }
}
