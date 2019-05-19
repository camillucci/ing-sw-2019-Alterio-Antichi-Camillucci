package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.network.socket.RemoteActionSocket;

import java.util.ArrayList;
import java.util.List;

public class CLIMessenger {
    private static final String PRESS = "Press ";
    private static final String PLAYER = "Player ";

    public static void incorrectInput() {
        System.out.println("Your answer is not valid, please try again.");
    }

    public static void insertName() {
        System.out.println("Insert username here:");
    }

    public void askConnection() {
        System.out.println("Choose connection type:");
        System.out.println(PRESS + "0 for Socket");
        System.out.println(PRESS + "1 for RMI");
    }

    public static void askInterface() {
        System.out.println("Choose interface type");
        System.out.println(PRESS + "0 for CLI");
        System.out.println(PRESS + "1 for GUI");
    }

    public static int askColor(List<String> availableColors) {
        System.out.println("Choose one of the available colors");
        for (int i = 0; i < availableColors.size(); i++) {
            System.out.println(PRESS + i + " if you want the color" + availableColors.get(i));
        }

        return availableColors.size();
    }

    public static void askGameLenght() {
        System.out.println("Choose how many skulls your game is going to have");
        System.out.println("You can choose any number between 5 and 8");
    }

    public static void askGameMap() {
        System.out.println("Choose one of the following maps");
        System.out.println("todo show maps");
        //TODO add maps to show
    }

    public static void matchStart() {
        System.out.println("Your game has started");
        System.out.println("Have fun!");
    }

    public static void threePlayers() {
        System.out.println("Your room has reached 3 players");
        System.out.println("countdown has started");
    }

    public static void displayMapOptions(int mapType) {
        //TODO add displayable map for every int between 0 and 2
    }

    /*
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

    Possibly cancel this and use a more generic method for all actions
     */

    private static void displayMap(int mapType) {
        //TODO display map based on selected type
    }

    private static void displayBoardState(MatchSnapshot matchSnapshot) {
        displayMap(matchSnapshot.gameBoardSnapshot.mapType);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (!(matchSnapshot.gameBoardSnapshot.squareSnapshots[i][j].ammoSquare))
                    System.out.println("Weapon Square -" + i + "|" + j + " - Current available weapons on this square are:" + matchSnapshot.gameBoardSnapshot.squareSnapshots[i][j].getCards().get(0) + "," + matchSnapshot.gameBoardSnapshot.squareSnapshots[i][j].getCards().get(1) + "," + matchSnapshot.gameBoardSnapshot.squareSnapshots[i][j].getCards().get(2));
                else
                    System.out.println("Ammo Square -" + i + "|" + j + " - Current available ammo card on this square is:" + matchSnapshot.gameBoardSnapshot.squareSnapshots[i][j].getCards().get(0));
                System.out.println("The following players are on this square:");
                for (int k = 0; k < matchSnapshot.gameBoardSnapshot.squareSnapshots[i][j].getColors().size(); k++)
                    System.out.println(matchSnapshot.gameBoardSnapshot.squareSnapshots[i][j].getColors().get(k));
                if (matchSnapshot.gameBoardSnapshot.squareSnapshots[i][j].getColors().isEmpty())
                    System.out.println("None");
            }
        }
    }

    public static void printMessage(String message)
    {
        System.out.println(message);
    }

    private static void displayPlayers(MatchSnapshot matchSnapshot) {
        displayPrivatePlayer(matchSnapshot);
        displayPublicPlayers(matchSnapshot);
    }

    public static  void updateView(MatchSnapshot matchSnapshot) {
        displayBoardState(matchSnapshot);
        displayPlayers(matchSnapshot);
    }

    private static void displayPrivatePlayer(MatchSnapshot matchSnapshot) {
        System.out.println("You (" + matchSnapshot.privatePlayerSnapshot.name + "-" + matchSnapshot.privatePlayerSnapshot.color + ") have the following loaded weapons:");
        for (int i = 0; i < matchSnapshot.privatePlayerSnapshot.getLoadedWeapons().size(); i++)
            System.out.println(matchSnapshot.privatePlayerSnapshot.getLoadedWeapons().get(i));
        System.out.println("You have the following unloaded weapons:");
        for (int i = 0; i < matchSnapshot.privatePlayerSnapshot.getUnloadedWeapons().size(); i++)
            System.out.println(matchSnapshot.privatePlayerSnapshot.getUnloadedWeapons().get(i));
        System.out.println("You have the following powerup cards:");
        for (int i = 0; i < matchSnapshot.privatePlayerSnapshot.getPowerUps().size(); i++)
            System.out.println(matchSnapshot.privatePlayerSnapshot.getPowerUps().get(i));
        System.out.println("Deaths:" + matchSnapshot.privatePlayerSnapshot.skull + "Blue ammo available:" + matchSnapshot.privatePlayerSnapshot.blueAmmo + "Red ammo available:" + matchSnapshot.privatePlayerSnapshot.redAmmo + "Yellow ammo available:" + matchSnapshot.privatePlayerSnapshot.yellowAmmo);
        //TODO add way to show mark and damage
    }

    private static void displayPublicPlayers(MatchSnapshot matchSnapshot) {
        for (int j = 0; j < matchSnapshot.getPublicPlayerSnapshot().size(); j++) {
            System.out.println(PLAYER + matchSnapshot.getPublicPlayerSnapshot().get(j).name + "-" + matchSnapshot.getPublicPlayerSnapshot().get(j).color + "has" + matchSnapshot.getPublicPlayerSnapshot().get(j).loadedWeaponsNumber + "loaded weapons:");
            System.out.println(PLAYER + matchSnapshot.getPublicPlayerSnapshot().get(j).name + "-" + matchSnapshot.getPublicPlayerSnapshot().get(j).color + "has the following unloaded weapons:");
            for (int i = 0; i < matchSnapshot.getPublicPlayerSnapshot().get(j).getUnloadedWeapons().size(); i++)
                System.out.println(matchSnapshot.privatePlayerSnapshot.getUnloadedWeapons().get(i));
            System.out.println(PLAYER + matchSnapshot.getPublicPlayerSnapshot().get(j).name + "-" + matchSnapshot.getPublicPlayerSnapshot().get(j).color + "has" + matchSnapshot.getPublicPlayerSnapshot().get(j).powerUpsNumber + "powerup cards");
            System.out.println("Deaths:" + matchSnapshot.getPublicPlayerSnapshot().get(j).skull + "Blue ammo available:" + matchSnapshot.getPublicPlayerSnapshot().get(j).blueAmmo + "Red ammo available:" + matchSnapshot.getPublicPlayerSnapshot().get(j).redAmmo + "Yellow ammo available:" + matchSnapshot.getPublicPlayerSnapshot().get(j).yellowAmmo);
            //TODO add way to show mark and damage
        }
    }

    public static void displayActions(ArrayList<RemoteAction> options) {
        for(int i = 0; i < options.size(); i++){
            System.out.println(PRESS + i + " if you want to execute action" + options.get(i).toString());
        }
    }

    public static int displayTargets(ArrayList<String> targetPlayers, ArrayList<String> targetSquares) {
        int j = targetPlayers.size();
        for(int i = 0; i < targetPlayers.size(); i++) {
            System.out.println(PRESS + i + " if you want to target" + targetPlayers.get(i));
        }
        for(int i = 0; i < targetSquares.size(); i++) {
            j = i + targetPlayers.size();
            System.out.println(PRESS + j + " if you want to move in the square" + targetSquares.get(i));
        }

        return j;
    }

    public static void displayTargetsAndAction(ArrayList<String> targetPlayers, ArrayList<String> targetSquares) {
        int temp = displayTargets(targetPlayers, targetSquares);
        System.out.println(PRESS + temp + " if you want to execute action with previously selected targets");
    }
}
