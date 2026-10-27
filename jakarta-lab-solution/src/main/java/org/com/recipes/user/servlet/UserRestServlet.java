package org.com.recipes.user.servlet;

import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.ws.rs.core.MediaType;
import org.com.recipes.core.dto.ListResponseDto;
import org.com.recipes.core.util.ImageTypeDetector;
import org.com.recipes.user.User;
import org.com.recipes.user.dto.CreateUserDto;
import org.com.recipes.user.mapper.UserMapper;
import org.com.recipes.user.service.UserService;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "UserRestServlet", urlPatterns = "/api/servlet/users/*")
@MultipartConfig
public class UserRestServlet extends HttpServlet {

    private final UserService userService;
    private final Jsonb jsonb;

    @Inject
    public UserRestServlet(UserService userService, Jsonb jsonb) {
        this.userService = userService;
        this.jsonb = jsonb;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            jsonb.toJson(new ListResponseDto<>(UserMapper.toUserDto(userService.provideUsers())), resp.getWriter());
        }
        else if (!path.endsWith("/avatar")) {
            String id = path.substring(1);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            jsonb.toJson(UserMapper.toUserDto(userService.provideUser(UUID.fromString(id))), resp.getWriter());
        }
        else {
            String id = path.substring(1).split("/")[0];
            byte[] avatar = userService.provideAvatar(UUID.fromString(id));
            resp.setContentType(ImageTypeDetector.detectImageType(avatar));
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentLength(avatar.length);
            resp.getOutputStream().write(avatar);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String contentType = req.getContentType();
        String path = req.getPathInfo();
        if (contentType != null && contentType.equals(MediaType.MULTIPART_FORM_DATA)) {
            Part userPart = req.getPart("user");
            Part avatarPart = req.getPart("avatar");
            User user = UserMapper.toEntity(jsonb.fromJson(userPart.getInputStream(), CreateUserDto.class));
            userService.createUser(user, avatarPart.getInputStream());
            resp.setHeader("Location", req.getServletPath() + "/" + user.getId());
        }
        else if (path != null && path.endsWith("/avatar")) {
            String id = path.substring(1).split("/")[0];
            userService.updateAvatar(UUID.fromString(id), req.getInputStream());
            resp.setHeader("Location", req.getServletPath() + path);
        }
        else {
            User user = UserMapper.toEntity(jsonb.fromJson(req.getInputStream(), CreateUserDto.class));
            userService.createUser(user, null);
            resp.setHeader("Location", req.getServletPath() + "/" + user.getId());
        }
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path.endsWith("/avatar")) {
            String id = path.substring(1).split("/")[0];
            userService.updateAvatar(UUID.fromString(id), req.getInputStream());
        }
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String path = req.getPathInfo();
        if (!path.endsWith("/avatar")) {
            userService.deleteUser(UUID.fromString(path.substring(1)));
        }
        else  {
            userService.deleteAvatar(UUID.fromString(path.substring(1).split("/")[0]));
        }
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
