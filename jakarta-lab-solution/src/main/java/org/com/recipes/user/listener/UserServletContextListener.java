package org.com.recipes.user.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.com.recipes.core.data.DataStore;
import org.com.recipes.user.repository.UserRepository;
import org.com.recipes.user.service.UserDefaultService;
import org.com.recipes.user.service.api.UserService;

import java.nio.file.Path;

@WebListener
public class UserServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DataStore dataStore = (DataStore) sce.getServletContext().getAttribute(DataStore.class.getName());
        dataStore.init();

        String directory = sce.getServletContext().getInitParameter("avatarDirectory");
        UserRepository userRepository = new UserRepository(dataStore, Path.of(directory));
        UserService userService = new UserDefaultService(userRepository);
        sce.getServletContext().setAttribute(UserRepository.class.getName(), userRepository);
        sce.getServletContext().setAttribute(UserService.class.getName(), userService);
    }
}
