package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.Square;

import java.util.ArrayList;

public class MessengerCLI {
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

    public void displayRollback() {
        //TODO
    }

    public void displayOptions(ArrayList<Player> players, ArrayList<Square> squares) {
        //TODO
    }

}
