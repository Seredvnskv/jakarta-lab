package org.com.recipes.user.servlet;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.com.recipes.user.User;
import org.com.recipes.user.mapper.UserMapper;
import org.com.recipes.user.service.api.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserRestServlet", urlPatterns = "/api/servlet/users/*")
@MultipartConfig
public class UserRestServlet extends HttpServlet {

    private UserService userService;
    private Jsonb jsonb;

    @Override
    public void init() {
        jsonb = JsonbBuilder.create();
        userService = (UserService) getServletContext().getAttribute(UserService.class.getName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<User> users = userService.findAllUsers();
        resp.setContentType("application/json");
        jsonb.toJson(UserMapper.toUserDto(users), resp.getWriter());
    }

    @Override
    public void destroy() {
        jsonb = null;
    }
}
