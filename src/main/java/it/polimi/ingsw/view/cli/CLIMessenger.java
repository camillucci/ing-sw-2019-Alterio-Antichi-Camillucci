package it.polimi.ingsw.view.cli;

import java.util.List;

public class CLIMessenger {

    private static final String PRESS = "Press ";
    //TODO

    public void insertName() {
        System.out.println("Insert username here:");
    }

    public void askConnection() {
        System.out.println("Choose connection type:");
        System.out.println(PRESS + "0 for Socket");
        System.out.println(PRESS + "1 for RMI");
    }

    public void askInterface() {
        System.out.println("Choose interface type");
        System.out.println(PRESS + "0 for CLI");
        System.out.println(PRESS + "1 for GUI");
    }

    public int askColor(List<String> availableColors) {
        System.out.println("Choose one of the available colors");
        for(int i = 0; i < availableColors.size(); i++) {
            System.out.println(PRESS + i + " if you want the color" + availableColors.get(i));
        }

        return availableColors.size();
    }

    public void askGameLenght() {
        System.out.println("Choose how many skulls your game is going to have");
        System.out.println("You can choose any number between 5 and 8");
    }

    public void askGameMap() {
        System.out.println("Choose one of the following maps");
        //TODO add maps to show
    }

    public void matchStart() {
        System.out.println("Your game has started");
        System.out.println("Have fun!");
    }

    public void threePlayers() {
        System.out.println("Your room has reached 3 players");
        System.out.println("180 seconds countdown has started");
    }

    public void displayMap(int mapType) {
        //TODO add displayable map for every int between 0 and 2
    }

    public void displayTargetsPlayers(List<String> targets) {
        for(int i = 0; i < targets.size(); i++)
            System.out.println(PRESS + i + " if you want to target player" + targets.get(i));
    }

    public void displayTargetsSquares(List<String> targets) {
        for(int i = 0; i < targets.size(); i++)
            System.out.println(PRESS + i + " if you want to target square" + targets.get(i));
    }

    public void displayTargetsBoth(List<String> targetPlayers, List<String> targetSquares) {
        for(int i = 0; i < targetPlayers.size(); i++)
            System.out.println(PRESS + i + " if you want to target player" + targetPlayers.get(i));
        for(int i = 0; i + targetPlayers.size() < targetSquares.size(); i++)
            System.out.println(PRESS + i + targetPlayers.size() + " if you want to target square" + targetSquares.get(i));
    }

    public void spawn(List<String> powerupCards) {
        for(int i = 0, j = 0; i < powerupCards.size(); i = i + 2, j++)
            System.out.println(PRESS + j + " if you want to discard the powerup card" + powerupCards.get(i) + "|" + powerupCards.get(i + 1));
    }
}
