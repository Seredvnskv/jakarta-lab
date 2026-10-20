package org.com.recipes.core.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

@ApplicationScoped
public final class JsonbProvider {

    @Produces
    @ApplicationScoped
    public Jsonb provideJsonb() {
        return JsonbBuilder.create();
    }

    public void close(@Disposes Jsonb jsonb) {
        try {
            if (jsonb != null) {
                jsonb.close();
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
