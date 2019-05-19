package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;
import it.polimi.ingsw.network.RemoteAction;

import java.util.ArrayList;
import java.util.List;

public class CLIMessenger {
    private static final String PRESS = "Press ";
    private static final String PLAYER = "Player ";
    private static final String NONE = "None";
    private static final int MAX_SIZE_SQUARE = 47;
    private static final String BLANK = " ";
    private static final String HORIZONTAL = "═";
    private static final String VERTICAL = "║";
    private static final String TOP_LEFT = "╔";
    private static final String TOP_RIGHT = "╗";
    private static final String BOTTOM_LEFT = "╚";
    private static final String BOTTOM_RIGHT = "╝";
    private static final String TOP = "╦";
    private static final String BOTTOM = "╩";
    private static final String LEFT = "╠";
    private static final String RIGHT = "╣";
    private static final String MIDDLE = "╬";
    private static final String HORIZONTAL_WALL = "═══════════════════════════════════════════════";
    private static final String HORIZONTAL_DOOR = "═══════════════════╣       ╠═══════════════════";
    private static final String HORIZONTAL_ROOM = "═════                                     ═════";

    private CLIMessenger() { }

    public static void intro() {
        display("   _____  ________ _____________________ _______      _____  .____    .___ _______  ___________");
        display("  /  _  \\ \\______ \\\\______   \\_   _____/ \\      \\    /  _  \\ |    |   |   |\\      \\ \\_   _____/");
        display(" /  /_\\  \\ |    |  \\|       _/|    __)_  /   |   \\  /  /_\\  \\|    |   |   |/   |   \\ |    __)_ ");
        display("/    |    \\|    `   \\    |   \\|        \\/    |    \\/    |    \\    |___|   /    |    \\|        \\");
        display("\\____|__  /_______  /____|_  /_______  /\\____|__  /\\____|__  /_______ \\___\\____|__  /_______  /");
        display("        \\/        \\/       \\/        \\/         \\/         \\/        \\/           \\/        \\/ ");
    }

    public static void incorrectInput() {
        display("Your answer is not valid, please try again.");
    }

    public static void insertName() {
        display("Insert username here (max 16 character long):");
    }

    public static void askConnection() {
        display("Choose connection type:");
        display(PRESS + "0 for Socket");
        display(PRESS + "1 for RMI");
    }

    public static void askInterface() {
        display("Choose interface type");
        display(PRESS + "0 for CLI");
        display(PRESS + "1 for GUI");
    }

    public static int askColor(List<String> availableColors) {
        display("Choose one of the following available colors:");
        for (int i = 0; i < availableColors.size(); i++) {
            display(PRESS + i + " if you want the color " + availableColors.get(i));
        }
        return availableColors.size();
    }

    public static void askGameLenght() {
        display("Choose how many skulls your game is going to have");
        display("You can choose any number between 5 and 8");
    }

    public static void askGameMap() {
        display("Choose one of the following maps:");
        display("todo show maps");
        //TODO add maps to show
    }

    public static void matchStart() {
        display("Your game has started");
        display("Have fun!");
    }

    public static void threePlayers() {
        display("Your room has reached 3 players");
        display("Countdown has started");
    }

    /*
    public static void displayTargetsPlayers(List<String> targets) {
        for(int i = 0; i < targets.size(); i++)
            display(PRESS + i + " if you want to target player" + targets.get(i));
    }

    public static void displayTargetsSquares(List<String> targets) {
        for(int i = 0; i < targets.size(); i++)
            display(PRESS + i + " if you want to target square" + targets.get(i));
    }

    public static void displayTargetsBoth(List<String> targetPlayers, List<String> targetSquares) {
        for(int i = 0; i < targetPlayers.size(); i++)
            display(PRESS + i + " if you want to target player" + targetPlayers.get(i));
        for(int i = 0; i + targetPlayers.size() < targetSquares.size(); i++)
            display(PRESS + i + targetPlayers.size() + " if you want to target square" + targetSquares.get(i));
    }

    Possibly cancel this and use a more generic method for all actions
    */

    /*
    private static void displayBoardState(MatchSnapshot matchSnapshot) {
        displayMap(matchSnapshot.gameBoardSnapshot.mapType);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (!(matchSnapshot.gameBoardSnapshot.squareSnapshots[i][j].ammoSquare))
                    display("Weapon Square -" + i + "|" + j + " - Current available weapons on this square are:" + matchSnapshot.gameBoardSnapshot.squareSnapshots[i][j].getCards().get(0) + "," + matchSnapshot.gameBoardSnapshot.squareSnapshots[i][j].getCards().get(1) + "," + matchSnapshot.gameBoardSnapshot.squareSnapshots[i][j].getCards().get(2));
                else
                    display("Ammo Square -" + i + "|" + j + " - Current available ammo card on this square is:" + matchSnapshot.gameBoardSnapshot.squareSnapshots[i][j].getCards().get(0));
                display("The following players are on this square:");
                for (int k = 0; k < matchSnapshot.gameBoardSnapshot.squareSnapshots[i][j].getColors().size(); k++)
                    display(matchSnapshot.gameBoardSnapshot.squareSnapshots[i][j].getColors().get(k));
                if (matchSnapshot.gameBoardSnapshot.squareSnapshots[i][j].getColors().isEmpty())
                    display("None");
            }
        }
    }
    */

    public static void printMessage(String message)
    {
        display(message);
    }

    public static void updateView(MatchSnapshot matchSnapshot) {
        displayMap(matchSnapshot);
        displayPlayers(matchSnapshot);
    }

    private static void displayPlayers(MatchSnapshot matchSnapshot) {
        displayPrivatePlayer(matchSnapshot);
        displayPublicPlayers(matchSnapshot);
    }

    private static void displayPrivatePlayer(MatchSnapshot matchSnapshot) {
        display("You (" + matchSnapshot.privatePlayerSnapshot.name + "-" + matchSnapshot.privatePlayerSnapshot.color + ") have the following loaded weapons:");
        if(matchSnapshot.privatePlayerSnapshot.getLoadedWeapons().isEmpty())
            display(NONE);
        for (int i = 0; i < matchSnapshot.privatePlayerSnapshot.getLoadedWeapons().size(); i++)
            display(matchSnapshot.privatePlayerSnapshot.getLoadedWeapons().get(i));
        display("You have the following unloaded weapons:");
        if(matchSnapshot.privatePlayerSnapshot.getUnloadedWeapons().isEmpty())
            display(NONE);
        for (int i = 0; i < matchSnapshot.privatePlayerSnapshot.getUnloadedWeapons().size(); i++)
            display(matchSnapshot.privatePlayerSnapshot.getUnloadedWeapons().get(i));
        display("You have the following powerup cards:");
        if(matchSnapshot.privatePlayerSnapshot.getPowerUps().isEmpty())
            display(NONE);
        for (int i = 0; i < matchSnapshot.privatePlayerSnapshot.getPowerUps().size(); i++)
            display(matchSnapshot.privatePlayerSnapshot.getPowerUps().get(i));
        display("Deaths: " + matchSnapshot.privatePlayerSnapshot.skull + ", Blue ammo available: " + matchSnapshot.privatePlayerSnapshot.blueAmmo + ", Red ammo available: " + matchSnapshot.privatePlayerSnapshot.redAmmo + ", Yellow ammo available: " + matchSnapshot.privatePlayerSnapshot.yellowAmmo);
        //TODO add way to show mark and damage
        display("");
    }

    private static void displayPublicPlayers(MatchSnapshot matchSnapshot) {
        for (int j = 0; j < matchSnapshot.getPublicPlayerSnapshot().size(); j++) {
            display(PLAYER + matchSnapshot.getPublicPlayerSnapshot().get(j).name + "-" + matchSnapshot.getPublicPlayerSnapshot().get(j).color + " has " + matchSnapshot.getPublicPlayerSnapshot().get(j).loadedWeaponsNumber + " loaded weapons");
            display(PLAYER + matchSnapshot.getPublicPlayerSnapshot().get(j).name + "-" + matchSnapshot.getPublicPlayerSnapshot().get(j).color + " has the following unloaded weapons:");
            if(matchSnapshot.getPublicPlayerSnapshot().get(j).getUnloadedWeapons().isEmpty())
                display(NONE);
            for (int i = 0; i < matchSnapshot.getPublicPlayerSnapshot().get(j).getUnloadedWeapons().size(); i++)
                display(matchSnapshot.privatePlayerSnapshot.getUnloadedWeapons().get(i));
            display(PLAYER + matchSnapshot.getPublicPlayerSnapshot().get(j).name + "-" + matchSnapshot.getPublicPlayerSnapshot().get(j).color + " has " + matchSnapshot.getPublicPlayerSnapshot().get(j).powerUpsNumber + " powerup cards");
            display("Deaths: " + matchSnapshot.getPublicPlayerSnapshot().get(j).skull + ", Blue ammo available: " + matchSnapshot.getPublicPlayerSnapshot().get(j).blueAmmo + ", Red ammo available: " + matchSnapshot.getPublicPlayerSnapshot().get(j).redAmmo + ", Yellow ammo available: " + matchSnapshot.getPublicPlayerSnapshot().get(j).yellowAmmo);
            //TODO add way to show mark and damage
            display("");
        }
    }

    public static void displayActions(ArrayList<RemoteAction> options) {
        for(int i = 0; i < options.size(); i++){
            display(PRESS + i + " if you want to execute action" + options.get(i).toString());
        }
    }

    public static int displayTargets(ArrayList<String> targetPlayers, ArrayList<String> targetSquares) {
        int j = targetPlayers.size();
        for(int i = 0; i < targetPlayers.size(); i++) {
            display(PRESS + i + " if you want to target" + targetPlayers.get(i));
        }
        for(int i = 0; i < targetSquares.size(); i++) {
            j = i + targetPlayers.size();
            display(PRESS + j + " if you want to move in the square" + targetSquares.get(i));
        }

        return j;
    }

    public static void displayTargetsAndAction(ArrayList<String> targetPlayers, ArrayList<String> targetSquares) {
        int temp = displayTargets(targetPlayers, targetSquares);
        display(PRESS + temp + " if you want to execute action with previously selected targets");
    }

    private static void displayMap(MatchSnapshot matchSnapshot) {
        if(matchSnapshot.gameBoardSnapshot.mapType == 10)
            displaySmallMap(matchSnapshot);
        else if(matchSnapshot.gameBoardSnapshot.mapType == 11)
            displayMiddleMap(matchSnapshot);
        else
            displayLargeMap(matchSnapshot);
        display("");
    }

    private static void displaySmallMap(MatchSnapshot matchSnapshot) {
        // FIRST LINE
        display(TOP_LEFT + HORIZONTAL_WALL + TOP + HORIZONTAL_WALL + TOP + HORIZONTAL_WALL + TOP_RIGHT);
        // FIRST HORIZONTAL SQUARES
        display(VERTICAL + square(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][0], 1) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][1], 1) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][2], 1) + VERTICAL);
        display(VERTICAL + square(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][0], 2) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][1], 2) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][2], 2) + VERTICAL);
        display(VERTICAL + square(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][0], 3) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][1], 3) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][2], 3) + VERTICAL);
        display(VERTICAL + square(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][0], 4) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][1], 4) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][2], 4) + VERTICAL);
        display(VERTICAL + square(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][0], 5) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][1], 5) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][2], 5) + VERTICAL);
        display(VERTICAL + square(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][0], 6) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][1], 6) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][2], 6) + VERTICAL);
        display(VERTICAL + square(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][0], 7) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][1], 7) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][2], 7) + VERTICAL);
        // SECOND LINE
        display(LEFT + HORIZONTAL_DOOR + MIDDLE + HORIZONTAL_WALL + MIDDLE + HORIZONTAL_DOOR + MIDDLE + HORIZONTAL_WALL + TOP_RIGHT);
        // SECOND HORIZONTAL SQUARES
        display(VERTICAL + square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][0], 1) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][1], 1) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][2], 1) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][3], 1) + VERTICAL);
        display(VERTICAL + square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][0], 2) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][1], 2) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][2], 2) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][3], 2) + VERTICAL);
        display(VERTICAL + square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][0], 3) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][1], 3) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][2], 3) + BOTTOM +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][3], 3) + VERTICAL);
        display(VERTICAL + square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][0], 4) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][1], 4) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][2], 4) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][3], 4) + VERTICAL);
        display(VERTICAL + square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][0], 5) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][1], 5) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][2], 5) + TOP +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][3], 5) + VERTICAL);
        display(VERTICAL + square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][0], 6) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][1], 6) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][2], 6) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][3], 6) + VERTICAL);
        display(VERTICAL + square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][0], 7) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][1], 7) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][2], 7) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[1][3], 7) + VERTICAL);
        // THIRD LINE
        display(BOTTOM_LEFT + HORIZONTAL_WALL + MIDDLE + HORIZONTAL_DOOR + MIDDLE + HORIZONTAL_WALL + MIDDLE + HORIZONTAL_ROOM + RIGHT);
        // THIRD HORIZONTAL SQUARES
        display(BLANK + blanks().get(MAX_SIZE_SQUARE) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[2][1], 1) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[2][2], 1) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[2][3], 1) + VERTICAL);
        display(BLANK + blanks().get(MAX_SIZE_SQUARE) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[2][1], 2) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[2][2], 2) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[2][3], 2) + VERTICAL);
        display(BLANK + blanks().get(MAX_SIZE_SQUARE) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[2][1], 3) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[2][2], 3) + BOTTOM +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[2][3], 3) + VERTICAL);
        display(BLANK + blanks().get(MAX_SIZE_SQUARE) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[2][1], 4) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[2][2], 4) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[2][3], 4) + VERTICAL);
        display(BLANK + blanks().get(MAX_SIZE_SQUARE) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[2][1], 5) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[2][2], 5) + TOP +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[2][3], 5) + VERTICAL);
        display(BLANK + blanks().get(MAX_SIZE_SQUARE) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[2][1], 6) + BLANK +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[2][2], 6) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[2][3], 6) + VERTICAL);
        display(BLANK + blanks().get(MAX_SIZE_SQUARE) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[2][1], 7) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[2][2], 7) + VERTICAL +
                square(matchSnapshot.gameBoardSnapshot.squareSnapshots[2][3], 7) + VERTICAL);
        // FOURTH LINE
        display(BLANK + blanks().get(MAX_SIZE_SQUARE) + BOTTOM_LEFT + HORIZONTAL_WALL + BOTTOM + HORIZONTAL_WALL + BOTTOM + HORIZONTAL_WALL + BOTTOM_RIGHT);
    }

    private static void displayMiddleMap(MatchSnapshot matchSnapshot) {
        //TODO
    }

    private static void displayLargeMap(MatchSnapshot matchSnapshot) {
        //TODO
    }

    private static String square(SquareSnapshot square, int lineNumber) {
        int line = lineNumber - 1;
        if(line == 6)
            return BLANK + blanks().get(MAX_SIZE_SQUARE - square.name.length() - 4) + "[" + square.name + "]" + BLANK;
        if(square.getColors().size() > line && square.getCards().size() > line)
            return BLANK + "[" + square.getNames().get(line) + "-" + square.getColors().get(line) + "]" +
                    blanks().get(MAX_SIZE_SQUARE - square.getNames().get(line).length() - square.getColors().get(line).length() - square.getCards().get(line).length() - 7) +
                    "[" + square.getCards().get(line) + "]" + BLANK;
        if(square.getColors().size() > line && square.getCards().size() <= line)
            return BLANK + "[" + square.getNames().get(line) + "-" + square.getColors().get(line) + "]" +
                    blanks().get(MAX_SIZE_SQUARE - square.getNames().get(line).length() - square.getColors().get(line).length() - 4);
        if(square.getColors().size() <= line && square.getCards().size() > line)
            return blanks().get(MAX_SIZE_SQUARE - square.getCards().get(line).length() - 3) + "[" + square.getCards().get(line) + "]" + BLANK;
        return blanks().get(MAX_SIZE_SQUARE);
    }

    private static List<String> blanks() {
        List<String> blanks = new ArrayList<>();
        String temp = "";
        for(int i = 0; i < MAX_SIZE_SQUARE + 1; i++) {
            blanks.add(temp);
            temp = temp.concat(" ");
        }
        return blanks;
    }

    private static void display(String string) {
        System.out.println(string);
    }
}
