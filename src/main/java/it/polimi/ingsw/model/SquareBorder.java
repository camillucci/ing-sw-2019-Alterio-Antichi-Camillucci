package it.polimi.ingsw.model;

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
