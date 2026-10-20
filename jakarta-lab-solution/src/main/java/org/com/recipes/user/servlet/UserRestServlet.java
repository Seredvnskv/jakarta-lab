package org.com.recipes.user.servlet;

import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.com.recipes.core.data.InitializeData;
import org.com.recipes.core.exception.AlreadyExistsException;
import org.com.recipes.core.exception.InvalidDataException;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.core.util.ImageTypeDetector;
import org.com.recipes.user.User;
import org.com.recipes.user.mapper.UserMapper;
import org.com.recipes.user.service.UserService;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "UserRestServlet", urlPatterns = "/api/servlet/users/*")
@MultipartConfig
public class UserRestServlet extends HttpServlet {

    @Inject
    private UserService userService;

    @Inject
    private Jsonb jsonb;

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
        catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            getUsers(resp);
        } else if (path.endsWith("/avatar")) {
            provideAvatar(provideAvatarUserId(req), resp);
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

    private void provideAvatar(UUID id, HttpServletResponse resp) throws IOException {
        byte[] avatar = userService.getUserAvatar(id);
        resp.setContentType(ImageTypeDetector.detectImageType(avatar));
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

    private UUID provideUserId(HttpServletRequest req) {
        String[] segments = pathSegments(req);
        if (segments.length != 1) {
            throw new NotFoundException();
        }
        return parseUserId(segments[0]);
    }

    private UUID provideAvatarUserId(HttpServletRequest req) {
        String[] segments = pathSegments(req);
        if (segments.length != 2 || !"avatar".equals(segments[1])) {
            throw new NotFoundException();
        }
        return parseUserId(segments[0]);
    }

    private String[] pathSegments(HttpServletRequest req) {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            throw new NotFoundException();
        }
        return path.substring(1).split("/");
    }

    private UUID parseUserId(String id) {
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
