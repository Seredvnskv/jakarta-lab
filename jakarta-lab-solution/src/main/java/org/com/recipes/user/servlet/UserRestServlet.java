package org.com.recipes.user.servlet;

import jakarta.json.bind.Jsonb;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.com.recipes.core.exception.AlreadyExistsException;
import org.com.recipes.core.exception.InvalidDataException;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.user.User;
import org.com.recipes.user.mapper.UserMapper;
import org.com.recipes.user.service.api.UserService;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "UserRestServlet", urlPatterns = "/api/servlet/users/*")
@MultipartConfig
public class UserRestServlet extends HttpServlet {

    private UserService userService;
    private Jsonb jsonb;

    @Override
    public void init() {
        jsonb = (Jsonb) getServletContext().getAttribute(Jsonb.class.getName());
        userService = (UserService) getServletContext().getAttribute(UserService.class.getName());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            super.service(req, resp);
        }
        catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        catch (InvalidDataException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        catch (AlreadyExistsException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            getUsers(resp);
        } else if (path.endsWith("/avatar")) {
            getAvatar(provideAvatarUserId(req), resp);
        } else {
            getUser(provideUserId(req), resp);
        }
    }

    private void getUsers(HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        jsonb.toJson(UserMapper.toUserDto(userService.findAllUsers()), resp.getWriter());
    }

    private void getUser(UUID id, HttpServletResponse resp) throws IOException {
        User user = userService.findUserById(id);
        resp.setContentType("application/json");
        jsonb.toJson(UserMapper.toUserDto(user), resp.getWriter());
    }

    private void getAvatar(UUID id, HttpServletResponse resp) throws IOException {
        byte[] avatar = userService.getUserAvatar(id);
        resp.setContentType("image/png");
        resp.setContentLength(avatar.length);
        resp.getOutputStream().write(avatar);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        userService.createUserAvatar(provideAvatarUserId(req), readAvatar(req));
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setHeader("Location", req.getRequestURI());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        userService.updateUserAvatar(provideAvatarUserId(req), readAvatar(req));
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        userService.deleteUserAvatar(provideAvatarUserId(req));
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private UUID provideAvatarUserId(HttpServletRequest req) {
        String path = req.getPathInfo();
        if (!path.endsWith("/avatar")) {
            throw new NotFoundException();
        }
        return provideUserId(req);
    }

    private UUID provideUserId(HttpServletRequest req) {
        String path = req.getPathInfo().substring(1);
        String id = path.endsWith("/avatar") ? path.split("/")[0] : path;
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException(e);
        }
    }

    private byte[] readAvatar(HttpServletRequest req) throws IOException {
        byte[] data = req.getInputStream().readAllBytes();
        if (data.length == 0) throw new InvalidDataException();
        return data;
    }
}
