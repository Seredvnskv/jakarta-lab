package org.com.recipes.cuisine;

public enum Region {
    REDANIA,
    NILFGAARD,
    TEMERIA,
    TOUSSAINT,
    SKELLIGE,
    KOVIR,
    ZERRIKANIA;

    public static Region fromString(String s){
        return Region.valueOf(s.toUpperCase());
    }
}
