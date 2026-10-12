package org.com.recipes.core.data.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.com.recipes.core.data.DataStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@WebListener
public class DataStoreContextListener implements ServletContextListener {
    private final DataStore dataStore = new DataStore();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute(DataStore.class.getName(), dataStore);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Path avatarDirectory = Paths.get(sce.getServletContext().getInitParameter("avatarDirectory"));
        try (Stream<Path> files = Files.list(avatarDirectory)) {
            for (Path file : files.toList()) {
                Files.deleteIfExists(file);
            }
        }
        catch (IOException e) {
            throw new IllegalStateException("Could not delete avatar directory: " + avatarDirectory);
        }
    }
}
