package org.com.recipes.core.listener;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.SneakyThrows;

@WebListener
public class JsonbContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute(Jsonb.class.getName(), JsonbBuilder.create());
    }

    @SneakyThrows
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Jsonb jsonb = (Jsonb) sce.getServletContext().getAttribute(Jsonb.class.getName());
        if (jsonb != null) {
            jsonb.close();
        }
    }
}
