package org.com.recipes.core.entity;

import java.io.Serializable;

public interface EntityId<T> extends Serializable {
    T id();
}
