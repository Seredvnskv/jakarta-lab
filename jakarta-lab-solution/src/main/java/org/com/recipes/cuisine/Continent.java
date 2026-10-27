package org.com.recipes.cuisine;

public enum Continent {
    AFRICA,
    ANTARCTICA,
    ASIA,
    EUROPE,
    NORTH_AMERICA,
    OCEANIA,
    SOUTH_AMERICA;

    public static Continent fromString(String s){
        return Continent.valueOf(s.toUpperCase());
    }

    @Override
    public String toString() {
        String s = this.name();
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
