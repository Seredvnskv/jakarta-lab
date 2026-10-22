package org.com.recipes.core.data;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.BeforeDestroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import org.com.recipes.core.exception.NotFoundException;
import org.com.recipes.user.Role;
import org.com.recipes.user.User;
import org.com.recipes.user.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
@NoArgsConstructor
public class DataLoader {
    private UserService userService;

    private final URL triss = this.getClass().getResource("/assets/triss.png");
    private final URL ciri = this.getClass().getResource("/assets/ciri.jpg");
    private final URL geralt = this.getClass().getResource("/assets/geralt.jpg");
    private final URL jaskier = this.getClass().getResource("/assets/jaskier.jpg");
    private final URL zoltan = this.getClass().getResource("/assets/zoltan.jpg");

    @Inject
    public DataLoader(UserService userService) {
        this.userService = userService;
    }

    private final Set<User> users = Set.of(
            User.builder().id(UUID.fromString("6b04e739-3251-4f8a-a962-5e1d8b1a592d"))
                    .username("Triss")
                    .role(Role.ADMIN)
                    .registrationDate(LocalDate.now())
                    .avatar(provideAvatar(triss))
                    .build(),

            User.builder().id(UUID.fromString("58449fe0-5e39-42ab-b1a4-f94e8c50849d"))
                    .username("Ciri").role(Role.USER)
                    .registrationDate(LocalDate.now())
                    .avatar(provideAvatar(ciri))
                    .build(),

            User.builder().id(UUID.fromString("439af2a7-9196-429e-a02d-aedc09b66f96"))
                    .username("Geralt").role(Role.USER)
                    .registrationDate(LocalDate.now())
                    .avatar(provideAvatar(geralt))
                    .build(),

            User.builder().id(UUID.fromString("2ac72290-178b-495d-9c73-3fc3abe98533"))
                    .username("Jaskier")
                    .role(Role.USER)
                    .registrationDate(LocalDate.now())
                    .avatar(provideAvatar(jaskier))
                    .build(),

            User.builder().id(UUID.fromString("8285f6d1-3dc9-46a6-9c5d-0484634e5458"))
                    .username("Zoltan")
                    .role(Role.USER)
                    .registrationDate(LocalDate.now())
                    .avatar(provideAvatar(zoltan))
                    .build()
    );

    public void load(@Observes @Initialized(ApplicationScoped.class) Object init) {
        if (userService.provideUsers().isEmpty()) {
            users.forEach(userService::createUser);
        }
    }

    public void clean(@Observes @BeforeDestroyed(ApplicationScoped.class) Object init) {
        users.forEach(user -> {
            try {
                userService.deleteUser(user.getId());
            } catch (NotFoundException e) {}
        });
    }

    private byte[] provideAvatar(URL resource) {
        try (InputStream in = resource.openStream()) {
            return in.readAllBytes();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
