package org.com.recipes.core.util;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import lombok.Getter;

import java.nio.file.Path;

@ApplicationScoped
public class AvatarDirectoryProvider {

    @Inject
    private ServletContext servletContext;

    @Getter
    private Path avatarDirectory;

    public AvatarDirectoryProvider() {}

    @PostConstruct
    public void init() {
        this.avatarDirectory = Path.of(servletContext.getInitParameter("avatarDirectory"));
    }
}
