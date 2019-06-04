package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;
import it.polimi.ingsw.network.RemoteAction;

import java.util.ArrayList;
import java.util.List;

public class CLIMessenger {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    private static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    private static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    private static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    private static String curColor = "";
    private static final String PRESS = "Press ";
    private static final String PLAYER = "Player ";
    private static final String NONE = "None";
    private static final int MAX_SIZE_SQUARE = 41;
    private static final int FIRST_MAP = 0;
    private static final int SECOND_MAP = 1;
    private static final int THIRD_MAP = 2;
    private static final int MAP_HEIGHT = 3;
    private static final int MAP_WIDTH = 4;
    private static final int SQUARE_LINES = 7;

    private static final String BLANK = " ";
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
    private static final String HORIZONTAL_WALL = "═════════════════════════════════════════";
    private static final String HORIZONTAL_DOOR = "════════════════╣       ╠════════════════";
    private static final String HORIZONTAL_ROOM = "═════                               ═════";
    private static final String POINT = "█";

    private static final String[] WALL = {VERTICAL, VERTICAL, VERTICAL, VERTICAL, VERTICAL, VERTICAL, VERTICAL};
    private static final String[] ROOM = {VERTICAL, BLANK, BLANK, BLANK, BLANK, BLANK, VERTICAL};
    private static final String[] DOOR = {VERTICAL, VERTICAL, BOTTOM, BLANK, TOP, VERTICAL, VERTICAL};

    private CLIMessenger() { }

    private static void resetColor(){
        curColor = ANSI_WHITE;
    }

    public static void intro() {
        curColor = ANSI_RED;
        display("   _____  ________ _____________________ _______      _____  .____    .___ _______  ___________");
        display("  /  _  \\ \\______ \\\\______   \\_   _____/ \\      \\    /  _  \\ |    |   |   |\\      \\ \\_   _____/");
        display(" /  /_\\  \\ |    |  \\|       _/|    __)_  /   |   \\  /  /_\\  \\|    |   |   |/   |   \\ |    __)_ ");
        display("/    |    \\|    `   \\    |   \\|        \\/    |    \\/    |    \\    |___|   /    |    \\|        \\");
        display("\\____|__  /_______  /____|_  /_______  /\\____|__  /\\____|__  /_______ \\___\\____|__  /_______  /");
        display("        \\/        \\/       \\/        \\/         \\/         \\/        \\/           \\/        \\/ ");
        display("\n");
        resetColor();
    }

    public static void login(){
        curColor = ANSI_BLUE;
        display("Welcome to Adrenaline!" + ANSI_RESET +"\n\n", true);
        resetColor();
    }

    public static void incorrectInput() {
        display("Your answer is not valid, please try again.", true);
    }

    public static void insertName() {
        display("Insert username here (max 16 character long):", true);
    }

    public static void askConnection() {
        display("Choose connection type:", true);
        display(PRESS + "0 for Socket", true);
        display(PRESS + "1 for RMI", true);
    }

    public static void askInterface() {
        display("Choose interface type", true);
        display(PRESS + "0 for CLI", true);
        display(PRESS + "1 for GUI", true);
    }

    public static int askColor(List<String> availableColors) {
        display("Choose one of the following available colors:", true);
        for (int i = 0; i < availableColors.size(); i++) {
            display(PRESS + i + " if you want the color " + availableColors.get(i), true);
        }
        return availableColors.size();
    }

    public static void askGameLength() {
        display("Choose how many skulls your game is going to have", true);
        display("You can choose any number between 5 and 8", true);
    }

    public static void askGameMap() {
        display("Choose one of the following maps:", true);
        display("0 ╔══════════════════╦══════════════════╦══════════════════╗                    1 ╔══════════════════╦══════════════════╦══════════════════╦══════════════════╗");
        display("  ║                                                        ║                      ║                                                        ╩                  ║");
        display("  ║                                                        ║                      ║                                                                           ║");
        display("  ║                                                        ║                      ║                                                        ╦                  ║");
        display("  ╠══════╣    ╠══════╬══════════════════╬══════╣    ╠══════╬══════════════════╗   ╠══════╣    ╠══════╬══════════════════╬══════╣    ╠══════╬══════╣    ╠══════╣");
        display("  ║                                                        ╩                  ║   ║                                     ║                                     ║");
        display("  ║                                                                           ║   ║                                     ║                                     ║");
        display("  ║                                                        ╦                  ║   ║                                     ║                                     ║");
        display("  ╚══════════════════╬══════╣    ╠══════╬══════════════════╬══              ══╣   ╚══════════════════╬══════╣    ╠══════╬══              ══╬══              ══╣");
        display("                     ║                                     ╩                  ║                      ║                  ╩                                     ║");
        display("                     ║                                                        ║                      ║                                                        ║");
        display("                     ║                                     ╦                  ║                      ║                  ╦                                     ║");
        display("                     ╚══════════════════╩══════════════════╩══════════════════╝                      ╚══════════════════╩══════════════════╩══════════════════╝");
        display("");
        display("2 ╔══════════════════╦══════════════════╦══════════════════╗                    3 ╔══════════════════╦══════════════════╦══════════════════╦══════════════════╗");
        display("  ║                  ╩                                     ║                      ║                  ╩                                     ╩                  ║");
        display("  ║                                                        ║                      ║                                                                           ║");
        display("  ║                  ╦                                     ║                      ║                  ╦                                     ╦                  ║");
        display("  ╠══              ══╬══════╣    ╠══════╬══════╣    ╠══════╬══════════════════╗   ╠══════╣    ╠══════╬══════╣    ╠══════╬══════╣    ╠══════╬══════╣    ╠══════╣");
        display("  ║                  ║                                     ╩                  ║   ║                  ║                  ║                                     ║");
        display("  ║                  ║                                                        ║   ║                  ║                  ║                                     ║");
        display("  ║                  ║                                     ╦                  ║   ║                  ║                  ║                                     ║");
        display("  ╠══════╣    ╠══════╬══════╣    ╠══════╬══════════════════╬══              ══╣   ╠══════╣    ╠══════╬══════╣    ╠══════╬══              ══╬══              ══╣");
        display("  ║                                                        ╩                  ║   ║                                     ╩                                     ║");
        display("  ║                                                                           ║   ║                                                                           ║");
        display("  ║                                                        ╦                  ║   ║                                     ╦                                     ║");
        display("  ╚══════════════════╩══════════════════╩══════════════════╩══════════════════╝   ╚══════════════════╩══════════════════╩══════════════════╩══════════════════╝");
    }

    public static void matchStart() {
        display("Your game has started", true);
        display("Have fun!", true);
    }

    public static void threePlayers() {
        display("Your room has reached 3 players", true);
        display("Countdown has started", true);
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

    public static void printMessage(String message)
    {
        display(message);
    }

    //------------------------------------------------------------------------------------------------------------------
    // DISPLAY VIEW

    public static void updateView(MatchSnapshot matchSnapshot) {
        displayMap(matchSnapshot);
        displayPlayers(matchSnapshot);
    }

    private static void displayPlayers(MatchSnapshot matchSnapshot) {
        displayPrivatePlayer(matchSnapshot);
        displayPublicPlayers(matchSnapshot);
    }

    private static void displayPrivatePlayer(MatchSnapshot matchSnapshot) {
        display("You " + coloredString("[" + matchSnapshot.privatePlayerSnapshot.name + "-" + matchSnapshot.privatePlayerSnapshot.color + "]", matchSnapshot.privatePlayerSnapshot.color) + " have:");
        display("Loaded weapons:");
        if(matchSnapshot.privatePlayerSnapshot.getLoadedWeapons().isEmpty())
            display(NONE);
        for (int i = 0; i < matchSnapshot.privatePlayerSnapshot.getLoadedWeapons().size(); i++)
            display(matchSnapshot.privatePlayerSnapshot.getLoadedWeapons().get(i));
        display("Unloaded weapons:");
        if(matchSnapshot.privatePlayerSnapshot.getUnloadedWeapons().isEmpty())
            display(NONE);
        for (int i = 0; i < matchSnapshot.privatePlayerSnapshot.getUnloadedWeapons().size(); i++)
            display(matchSnapshot.privatePlayerSnapshot.getUnloadedWeapons().get(i));
        display("Powerup cards:");
        if(matchSnapshot.privatePlayerSnapshot.getPowerUps().isEmpty())
            display(NONE);
        for (int i = 0; i < matchSnapshot.privatePlayerSnapshot.getPowerUps().size(); i++)
            display(matchSnapshot.privatePlayerSnapshot.getPowerUps().get(i));
        display("Deaths: " + matchSnapshot.privatePlayerSnapshot.skull);
        display("Blue ammo: " + matchSnapshot.privatePlayerSnapshot.blueAmmo + ", Red ammo: " + matchSnapshot.privatePlayerSnapshot.redAmmo + ", Yellow ammo: " + matchSnapshot.privatePlayerSnapshot.yellowAmmo);

        String damage = "Damages: ";
        if(matchSnapshot.privatePlayerSnapshot.getDamage().isEmpty())
            damage = damage.concat(NONE);
        else
            for(int i = 0; i < matchSnapshot.privatePlayerSnapshot.getDamage().size(); i++)
                damage = damage.concat(coloredString(POINT, matchSnapshot.privatePlayerSnapshot.getDamage().get(i)) + " ");
        display(damage);
        String mark = "Marks: ";
        if(matchSnapshot.privatePlayerSnapshot.getMark().isEmpty())
            mark = mark.concat(NONE);
        else
            for(int i = 0; i < matchSnapshot.privatePlayerSnapshot.getMark().size(); i++)
                mark = mark.concat(coloredString(POINT, matchSnapshot.privatePlayerSnapshot.getMark().get(i)) + " ");
        display(mark);

        display("");
    }

    private static void displayPublicPlayers(MatchSnapshot matchSnapshot) {
        for (int j = 0; j < matchSnapshot.getPublicPlayerSnapshot().size(); j++) {
            display(PLAYER + coloredString(matchSnapshot.getPublicPlayerSnapshot().get(j).name + "-" + matchSnapshot.getPublicPlayerSnapshot().get(j).color, matchSnapshot.getPublicPlayerSnapshot().get(j).color) + " has:");
            display("Loaded weapons: " + matchSnapshot.getPublicPlayerSnapshot().get(j).loadedWeaponsNumber);
            display("Unloaded weapons:");
            if(matchSnapshot.getPublicPlayerSnapshot().get(j).getUnloadedWeapons().isEmpty())
                display(NONE);
            for (int i = 0; i < matchSnapshot.getPublicPlayerSnapshot().get(j).getUnloadedWeapons().size(); i++)
                display(matchSnapshot.privatePlayerSnapshot.getUnloadedWeapons().get(i));
            display("Powerup cards: " + matchSnapshot.getPublicPlayerSnapshot().get(j).powerUpsNumber);
            display("Deaths: " + matchSnapshot.getPublicPlayerSnapshot().get(j).skull);
            display("Blue ammo: " + matchSnapshot.getPublicPlayerSnapshot().get(j).blueAmmo + ", Red ammo: " + matchSnapshot.getPublicPlayerSnapshot().get(j).redAmmo + ", Yellow ammo: " + matchSnapshot.getPublicPlayerSnapshot().get(j).yellowAmmo);

            String damage = "Damages: ";
            if(matchSnapshot.getPublicPlayerSnapshot().get(j).getDamage().isEmpty())
                damage = damage.concat(NONE);
            else
                for(int i = 0; i < matchSnapshot.getPublicPlayerSnapshot().get(j).getDamage().size(); i++)
                    damage = damage.concat(coloredString(POINT, matchSnapshot.getPublicPlayerSnapshot().get(j).getDamage().get(i)) + " ");
            display(damage);
            String mark = "Marks: ";
            if(matchSnapshot.getPublicPlayerSnapshot().get(j).getMark().isEmpty())
                mark = mark.concat(NONE);
            else
                for(int i = 0; i < matchSnapshot.getPublicPlayerSnapshot().get(j).getMark().size(); i++)
                    mark = mark.concat(coloredString(POINT, matchSnapshot.getPublicPlayerSnapshot().get(j).getMark().get(i)) + " ");
            display(mark);

            display("");
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // DISPLAY ACTIONS AND TARGETS

    public static void displayActions(List<RemoteAction> options) {
        for(RemoteAction remoteAction : options){
            display(PRESS + remoteAction.index + " if you want to " + remoteAction.text);
        }
    }

    public static int displayTargets(ArrayList<String> targetPlayers, ArrayList<String> targetSquares, ArrayList<String> usablePowerUps, ArrayList<String> discardablePowerUps) {
        int j = 0;
        for(int i = 0; i < targetPlayers.size(); i++, j++)
            display(PRESS + j + " if you want to target player " + targetPlayers.get(i));
        for(int i = 0; i < targetSquares.size(); i++, j++)
            display(PRESS + j + " if you want to target square " + targetSquares.get(i));
        for(int i = 0; i < usablePowerUps.size(); i++, j++)
            display(PRESS + j + " if you want to use " + targetSquares.get(i));
        for(int i = 0; i < discardablePowerUps.size(); i++, j++)
            display(PRESS + j + " if you want to discard " + targetSquares.get(i));
        return j;
    }

    public static void displayTargetsAndAction(ArrayList<String> targetPlayers, ArrayList<String> targetSquares, ArrayList<String> usablePowerUps, ArrayList<String> discardablePowerUps) {
        int temp = displayTargets(targetPlayers, targetSquares, usablePowerUps, discardablePowerUps);
        display(PRESS + temp + " if you want to execute action with previously selected targets");
    }

    //------------------------------------------------------------------------------------------------------------------
    // DISPLAY MAP

    private static void displayMap(MatchSnapshot matchSnapshot) {
        String[] mapBorder;
        if(matchSnapshot.gameBoardSnapshot.mapType == FIRST_MAP)
            mapBorder = firstMap;
        else if(matchSnapshot.gameBoardSnapshot.mapType == SECOND_MAP)
            mapBorder = secondMap;
        else if(matchSnapshot.gameBoardSnapshot.mapType == THIRD_MAP)
            mapBorder = thirdMap;
        else
            mapBorder = fourthMap;
        for(int i = 0; i < MAP_HEIGHT; i++) {
            display(mapBorder[i]);
            displaySquareLine(matchSnapshot.gameBoardSnapshot.squareSnapshots[i]);
        }
        display(mapBorder[MAP_HEIGHT]);
        display("");
    }

    private static String[] firstMap = {
            TOP_LEFT + HORIZONTAL_WALL + TOP + HORIZONTAL_WALL + TOP + HORIZONTAL_WALL + TOP_RIGHT,
            LEFT + HORIZONTAL_DOOR + MIDDLE + HORIZONTAL_WALL + MIDDLE + HORIZONTAL_DOOR + MIDDLE + HORIZONTAL_WALL + TOP_RIGHT,
            BOTTOM_LEFT + HORIZONTAL_WALL + MIDDLE + HORIZONTAL_DOOR + MIDDLE + HORIZONTAL_WALL + MIDDLE + HORIZONTAL_ROOM + RIGHT,
            BLANK + blanks(MAX_SIZE_SQUARE) + BOTTOM_LEFT + HORIZONTAL_WALL + BOTTOM + HORIZONTAL_WALL + BOTTOM + HORIZONTAL_WALL + BOTTOM_RIGHT
    };

    private static String[] secondMap = {
            TOP_LEFT + HORIZONTAL_WALL + TOP + HORIZONTAL_WALL + TOP + HORIZONTAL_WALL + TOP + HORIZONTAL_WALL + TOP_RIGHT,
            LEFT + HORIZONTAL_DOOR + MIDDLE + HORIZONTAL_WALL + MIDDLE + HORIZONTAL_DOOR + MIDDLE + HORIZONTAL_DOOR + RIGHT,
            BOTTOM_LEFT + HORIZONTAL_WALL + MIDDLE + HORIZONTAL_DOOR + MIDDLE + HORIZONTAL_ROOM + MIDDLE + HORIZONTAL_ROOM + RIGHT,
            BLANK + blanks(MAX_SIZE_SQUARE) + BOTTOM_LEFT + HORIZONTAL_WALL + BOTTOM + HORIZONTAL_WALL + BOTTOM + HORIZONTAL_WALL + BOTTOM_RIGHT
    };

    private static String[] thirdMap = {
            TOP_LEFT + HORIZONTAL_WALL + TOP + HORIZONTAL_WALL + TOP + HORIZONTAL_WALL + TOP_RIGHT,
            LEFT + HORIZONTAL_ROOM + MIDDLE + HORIZONTAL_DOOR + MIDDLE + HORIZONTAL_DOOR + MIDDLE + HORIZONTAL_WALL + TOP_RIGHT,
            LEFT + HORIZONTAL_DOOR + MIDDLE + HORIZONTAL_DOOR + MIDDLE + HORIZONTAL_WALL + MIDDLE + HORIZONTAL_ROOM + RIGHT,
            BOTTOM_LEFT + HORIZONTAL_WALL + BOTTOM + HORIZONTAL_WALL + BOTTOM + HORIZONTAL_WALL + BOTTOM + HORIZONTAL_WALL + BOTTOM_RIGHT
    };

    private static String[] fourthMap = {
            TOP_LEFT + HORIZONTAL_WALL + TOP + HORIZONTAL_WALL + TOP + HORIZONTAL_WALL + TOP + HORIZONTAL_WALL + TOP_RIGHT,
            LEFT + HORIZONTAL_DOOR + MIDDLE + HORIZONTAL_DOOR + MIDDLE + HORIZONTAL_DOOR + MIDDLE + HORIZONTAL_DOOR + RIGHT,
            LEFT + HORIZONTAL_DOOR + MIDDLE + HORIZONTAL_DOOR + MIDDLE + HORIZONTAL_ROOM + MIDDLE + HORIZONTAL_ROOM + RIGHT,
            BOTTOM_LEFT + HORIZONTAL_WALL + BOTTOM + HORIZONTAL_WALL + BOTTOM + HORIZONTAL_WALL + BOTTOM + HORIZONTAL_WALL + BOTTOM_RIGHT
    };

    private static void displaySquareLine(SquareSnapshot[] squareSnapshots) {
        for(int i = 1; i <= SQUARE_LINES; i++)
            display(horizontalLine(squareSnapshots, i));
    }

    private static String horizontalLine(SquareSnapshot[] squareSnapshots, int line) {
        String temp = leftSide(squareSnapshots[0], line);
        for(int i = 0; i < MAP_WIDTH; i++)
            if(squareSnapshots[i] == null) {
                temp = temp.concat(blanks(MAX_SIZE_SQUARE));
                if(i == 0)
                    temp = temp.concat(VERTICAL);
            }
            else {
                temp = temp.concat(square(squareSnapshots[i], line));
                temp = temp.concat(rightSide(squareSnapshots[i], line));
            }
        return temp;
    }

    private static String leftSide(SquareSnapshot squareSnapshot, int line) {
        if(squareSnapshot == null)
            return BLANK;
        switch(squareSnapshot.west) {
            case "Room": return ROOM[line - 1];
            case "Door": return DOOR[line - 1];
            default: return WALL[line - 1];
        }
    }

    private static String rightSide(SquareSnapshot squareSnapshot, int line) {
        if(squareSnapshot == null)
            return BLANK;
        switch(squareSnapshot.east) {
            case "Room": return ROOM[line - 1];
            case "Door": return DOOR[line - 1];
            default: return WALL[line - 1];
        }
    }

    private static String square(SquareSnapshot square, int lineNumber) {
        int line = lineNumber - 1;
        if(line == SQUARE_LINES - 1)
            return BLANK + blanks(MAX_SIZE_SQUARE - square.name.length() - 4) + "[" + square.name + "]" + BLANK;
        if(square != null) {
            if (square.getColors().size() > line && square.getCards().size() > line)
                return BLANK + coloredString("[" + square.getNames().get(line) + "]", square.getColors().get(line)) +
                        blanks(MAX_SIZE_SQUARE - square.getNames().get(line).length() - square.getCards().get(line).length() - 6) +
                        "[" + square.getCards().get(line) + "]" + BLANK;
            if (square.getColors().size() > line && square.getCards().size() <= line)
                return BLANK + coloredString("[" + square.getNames().get(line) + "]", square.getColors().get(line)) +
                        blanks(MAX_SIZE_SQUARE - square.getNames().get(line).length() - 5);
            if (square.getColors().size() <= line && square.getCards().size() > line)
                return blanks(MAX_SIZE_SQUARE - square.getCards().get(line).length() - 3) + "[" + square.getCards().get(line) + "]" + BLANK;
        }
        return blanks(MAX_SIZE_SQUARE);
    }

    private static String blanks(int num) {
        String temp = "";
        for(int i = 0; i < num; i++) {
            temp = temp.concat(" ");
        }
        return temp;
    }

    //------------------------------------------------------------------------------------------------------------------
    //DISPLAY

    @SuppressWarnings("squid:S106")
    private static void display(String string) {
        System.out.println(curColor + string);
    }

    private static void display(String string, boolean bold){
        if(bold)
            display("\u001B[1m" + string);
        else
            display(string);
    }

    private static String coloredString(String text, String color) {
        switch (color) {
            case "Blue":
                return ANSI_BLUE + text + ANSI_WHITE;
            case "Green":
                return  ANSI_GREEN + text + ANSI_WHITE;
            case "Grey":
                return  ANSI_WHITE + text + ANSI_WHITE; // Change to GREY
            case "Violet":
                return  ANSI_PURPLE + text + ANSI_WHITE;
            default:
                return  ANSI_YELLOW + text + ANSI_WHITE;
        }
    }
}
