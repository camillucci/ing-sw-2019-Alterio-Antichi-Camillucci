package it.polimi.ingsw.model;

/**
 * This class contains the border types that can be found between squares
 */
public enum SquareBorder {
    DOOR("Door"),
    WALL("Wall"),
    ROOM("Room"),
    NOTHING("Nothing");

    private String name;

    SquareBorder(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
