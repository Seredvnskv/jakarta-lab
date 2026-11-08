package org.com.recipes.cuisine.entity;

public enum Continent {
    ASIA,
    AFRICA,
    AMERICA,
    EUROPE,
    OCEANIA;

    public static Continent fromString(String name) {
        return Continent.valueOf(name);
    }

    @Override
    public String toString() {
        String name = this.name();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
