package it.polimi.ingsw.model;

/**
 * This class contains the colors Players can assume
 */
public enum PlayerColor {
    BLUE("Blue"),
    GREEN("Green"),
    GREY("Grey"),
    VIOLET("Violet"),
    YELLOW("Yellow");

    private String name;

    PlayerColor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
