package it.polimi.ingsw.model;

/**
 * This class contains the colors WeaponCards, PowerUpCards and Squares can assume
 */
public enum AmmoColor {
    BLUE("Blue"),
    RED("Red"),
    YELLOW("Yellow");

    private String name;

    AmmoColor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
