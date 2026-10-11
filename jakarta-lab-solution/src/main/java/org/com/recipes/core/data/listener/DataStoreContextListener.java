package org.com.recipes.core.data.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.com.recipes.core.data.DataStore;

@WebListener
public class DataStoreContextListener implements ServletContextListener {
    private final DataStore dataStore = new DataStore();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute(DataStore.class.getName(), dataStore);
    }
}
