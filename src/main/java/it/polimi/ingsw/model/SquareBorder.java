package it.polimi.ingsw.model;

/**
 * This class contains the border type that links Squares between them
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
