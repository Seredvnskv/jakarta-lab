package org.com.recipes.core.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.com.recipes.core.exception.InvalidDataException;
import org.com.recipes.core.exception.NotFoundException;

import java.io.IOException;

@WebFilter(urlPatterns = "/api/servlet/*")
public class ExceptionFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        }
        catch (NotFoundException nfe) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        catch (InvalidDataException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
