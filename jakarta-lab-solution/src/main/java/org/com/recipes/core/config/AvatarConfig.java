package org.com.recipes.core.config;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ApplicationScoped
@NoArgsConstructor
public class AvatarConfig {

    @Inject
    private ServletContext servletContext;

    @Getter
    private Path avatarPath;

    @PostConstruct
    public void init() {
        try {
            this.avatarPath = Path.of(servletContext.getInitParameter("avatarPath"));
            Files.createDirectories(avatarPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
