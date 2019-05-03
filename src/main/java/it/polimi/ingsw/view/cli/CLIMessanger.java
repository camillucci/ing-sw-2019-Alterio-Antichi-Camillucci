package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.Square;

import java.util.ArrayList;

public class CLIMessanger {
    //TODO

    public void insertName() {
        System.out.println("Insert username here:");
    }

    public void askConnection() {
        System.out.println("Choose connection type:");
        System.out.println("Press 0 for Socket");
        System.out.println("Press 1 for RMI");
    }

    public void askInterface() {
        System.out.println("Choose interface type");
        System.out.println("Press 0 for CLI");
        System.out.println("Press 1 for GUI");
    }

    public int askColor(ArrayList<PlayerColor> colors) {
        System.out.println("Choose one of the available colors");
        ArrayList<String> availableColors = new ArrayList<String>();
        for(PlayerColor pc : colors) {
            availableColors.add(pc.name());
        }
        for(int i = 0; i < availableColors.size(); i++) {
            System.out.println("Press" + i + "if you want the color" + availableColors.get(i));
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

    public void displayTargetsPlayers(ArrayList<Player> targets) {
        for(int i = 0; i < targets.size(); i++)
            System.out.println("Press" + i + "if you want to target player" + targets.get(i).getName());
    }

    public void displayTargetsSquares(ArrayList<Square> targets) {
        for(int i = 0; i < targets.size(); i++)
            System.out.println("Press" + i + "if you want to target player" + targets.get(i).getName());
    }

    public void displayTargetsBoth(ArrayList<Player> targetPlayers, ArrayList<Square> targetSquares) {
        for(int i = 0; i < targetPlayers.size(); i++)
            System.out.println("Press" + i + "if you want to target player" + targetPlayers.get(i).getName());
        for(int i = 0; i + targetPlayers.size() < targetSquares.size(); i++)
            System.out.println("Press" + i + targetPlayers.size() + "if you want to target player" + targetSquares.get(i).getName());
    }
}
