package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;
import it.polimi.ingsw.network.RemoteAction;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * This class is used to display messages which need to be communicated to the user. It is used only if the user has
 * chosen a CLI based display.
 */
public class CLIMessenger {
    /**
     * parameter used to produce output lines the user is going to read
     */
    private static PrintStream printStream = new PrintStream(System.out);

    /**
     * String that represents default color
     */
    private static final String ANSI_RESET = "\u001B[0m";

    /**
     * String that represents color "black"
     */
    private static final String ANSI_BLACK = "\u001B[30m";

    /**
     * String that represents color "red"
     */
    private static final String ANSI_RED = "\u001B[31m";

    /**
     * String that represents color "green"
     */
    private static final String ANSI_GREEN = "\u001B[32m";

    /**
     * String that represents color "yellow"
     */
    private static final String ANSI_YELLOW = "\u001B[33m";

    /**
     * String that represents color "blue"
     */
    private static final String ANSI_BLUE = "\u001B[34m";

    /**
     * String that represents color "purple"
     */
    private static final String ANSI_PURPLE = "\u001B[35m";

    /**
     * String that represents color "cyan"
     */
    private static final String ANSI_CYAN = "\u001B[36m";

    /**
     * String that represents color "grey"
     */
    private static final String ANSI_GREY = "\u001B[37m";

    /**
     * String that represents color "white"
     */
    private static final String ANSI_WHITE = "\u001B[37m";

    /**
     * String that represents "black" background color
     */
    private static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";

    /**
     * String that represents "red" background color
     */
    private static final String ANSI_RED_BACKGROUND = "\u001B[41m";

    /**
     * String that represents "green" background color
     */
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";

    /**
     * String that represents "yellow" background color
     */
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";

    /**
     * String that represents "blue" background color
     */
    private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";

    /**
     * String that represents "purple" background color
     */
    private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";

    /**
     * String that represents "cyan" background color
     */
    private static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";

    /**
     * String that represents "white" background color
     */
    private static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    /**
     * String used to keep track of the current color used to display characters
     */
    private static String curColor = "";

    /**
     * "Press" String
     */
    private static final String PRESS = "Press ";

    /**
     * "Player" String
     */
    private static final String PLAYER = "Player ";

    /**
     * "None" String
     */
    private static final String NONE = "None";
    private static final int MAX_SIZE_SQUARE = 45;

    /**
     * int representing the first map type (users can choose different maps for every game)
     */
    private static final int FIRST_MAP = 0;

    /**
     * int representing the second map type (users can choose different maps for every game)
     */
    private static final int SECOND_MAP = 1;

    /**
     * int representing the third map type (users can choose different maps for every game)
     */
    private static final int THIRD_MAP = 2;

    /**
     * int representing the height of the map
     */
    private static final int MAP_HEIGHT = 3;

    /**
     * int representing the width of the map
     */
    private static final int MAP_WIDTH = 4;

    /**
     * int representing the height of the map
     */
    private static final int SQUARE_LINES = 7;

    /**
     * int representing the maximum number of skulls
     */
    private static final int MAX_SKULLS = 8;

    /**
     * Visual representation of a blank space
     */
    private static final String BLANK = " ";

    /**
     * Visual representation of a vertical line
     */
    private static final String VERTICAL = "║";

    /**
     * Visual representation of a top left corner
     */
    private static final String TOP_LEFT = "╔";

    /**
     * Visual representation of a bottom left corner
     */
    private static final String TOP_RIGHT = "╗";

    /**
     * Visual representation of a bottom left corner
     */
    private static final String BOTTOM_LEFT = "╚";

    /**
     * Visual representation of a bottom right corner
     */
    private static final String BOTTOM_RIGHT = "╝";

    /**
     * Visual representation of a top "T" shaped cross
     */
    private static final String TOP = "╦";

    /**
     * Visual representation of a bottom "T" shaped cross
     */
    private static final String BOTTOM = "╩";

    /**
     * Visual representation of a left "T" shaped cross
     */
    private static final String LEFT = "╠";

    /**
     * Visual representation of a right "T" shaped cross
     */
    private static final String RIGHT = "╣";

    /**
     * Visual representation of a "+" shaped cross
     */
    private static final String MIDDLE = "╬";

    /**
     * Visual representation of an horizontal wall
     */
    private static final String HORIZONTAL_WALL = "═════════════════════════════════════════════";

    /**
     * Visual representation of an horizontal door
     */
    private static final String HORIZONTAL_DOOR = "══════════════════╣       ╠══════════════════";

    /**
     * Visual representation of the horizontal entrance of a room
     */
    private static final String HORIZONTAL_ROOM = "═════                                   ═════";

    /**
     * Visual representation of the KillShotTrack
     */
    private static final String KILLSHOTTRACK_WALL = "═════════";

    /**
     * Visual representation of a point, used for damages, marks and costs
     */
    private static final String POINT = "█";

    /**
     * Contains the list of strings used to visually represent a wall
     */
    private static final String[] WALL = {VERTICAL, VERTICAL, VERTICAL, VERTICAL, VERTICAL, VERTICAL, VERTICAL};

    /**
     * Contains the list of strings used to visually represent the entrance of a room
     */
    private static final String[] ROOM = {VERTICAL, BLANK, BLANK, BLANK, BLANK, BLANK, VERTICAL};

    /**
     * Contains the list of strings used to visually represent a door
     */
    private static final String[] DOOR = {VERTICAL, VERTICAL, BOTTOM, BLANK, TOP, VERTICAL, VERTICAL};

    private CLIMessenger() { }

    /**
     * Changes the current OutputStream with the one gotten has a parameter
     * @param outputStream parameter used to substitute the current OutputStream
     */
    public static void changePrintStream(OutputStream outputStream){
        printStream = new PrintStream(outputStream);
    }


    /**
     * Assigns the color "white" to the curColor parameter
     */
    private static void resetColor(){
        curColor = ANSI_WHITE;
    }

    /**
     * Default intro script (displayed when the client starts)
     */
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

    /**
     * Displays the welcome script to the user and resets the curColor parameter.
     */
    public static void login(){
        curColor = ANSI_BLUE;
        display("Welcome to Adrenaline!" + ANSI_RESET +"\n\n", true);
        resetColor();
    }

    /**
     * Output line displayed to the user when they send an incorrect input line
     */
    public static void incorrectInput() {
        display("Your answer is not valid, please try again.", true);
    }

    /**
     * Output line displayed to the user used to ask them to choose a username
     */
    public static void insertName() {
        display("Insert username here (max 16 character long):", true);
    }

    /**
     * Output line displayed to the user used to ask them to choose between Socket and RMI type of connection
     */
    public static void askConnection() {
        display("Choose connection type:", true);
        display(PRESS + "0 for Socket", true);
        display(PRESS + "1 for RMI", true);
    }

    /**
     * Output line displayed to the user used to ask them to choose between CLI and GUI type of interface
     */
    public static void askInterface() {
        display("Choose interface type", true);
        display(PRESS + "0 for CLI", true);
        display(PRESS + "1 for GUI", true);
    }

    /**
     * Output line displayed to the user used to ask them to choose a color for their character among the ones
     * still availabe
     * @param availableColors List of colors the user can choose from
     */
    public static int askColor(List<String> availableColors) {
        display("Choose one of the following available colors:", true);
        for (int i = 0; i < availableColors.size(); i++) {
            display(PRESS + i + " if you want the color " + availableColors.get(i), true);
        }
        return availableColors.size();
    }

    /**
     * Output line displayed to the user used to ask them to choose how long they want the game to be, based on
     * number of skulls. This message is displayed only if the user is the first one to connect to the room.
     */
    public static void askGameLength() {
        display("Choose how many skulls your game is going to have", true);
        display("You can choose any number between 5 and 8", true);
    }

    /**
     * Output line displayed to the user used to ask them to choose what map they want to play the game in (4
     * different options).
     * This message is displayed only if the user is the first one to connect to the room.
     */
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

    /**
     * Output line displayed to the user when the match has started (which occurs when the maximum number of player
     * connects to the same room, or when the timer runs out)
     */
    public static void matchStart() {
        display("Your game has started", true);
        display("Have fun!", true);
    }

    /**
     * Output line displayed to the user to communicate them that the room has reached the 3 player mark.
     * So the countdown starts.
     */
    public static void threePlayers() {
        display("Your room has reached 3 players", true);
        display("Countdown has started", true);
    }

    /**
     * Output line displayed to the user to communicate that they entered an illegal game state.
     */
    public static void rollbackError() {
        display("You entered an illegal state, please press 0 to return to the last valid state", true);
    }

    /**
     * Generic method used to print any string.
     * @param message String that is required to be displayed to the user.
     */
    public static void printMessage(String message)
    {
        display(message);
    }

    //------------------------------------------------------------------------------------------------------------------
    // DISPLAY VIEW

    /**
     * Calls 2 different methods which both display a part of the game state
     * @param matchSnapshot Contains all the necessary info for a completed display of the current game state.
     */
    public static void updateView(MatchSnapshot matchSnapshot) {
        displayKillShotTrack(matchSnapshot.gameBoardSnapshot.getKillShotTrack());
        displayMap(matchSnapshot);
        displayPlayers(matchSnapshot);
    }

    /**
     * Calls 2 different methods which display info relative to the players state.
     * @param matchSnapshot Contains all the necessary info for a completed display of the current game state.
     */
    private static void displayPlayers(MatchSnapshot matchSnapshot) {
        displayPrivatePlayer(matchSnapshot);
        displayPublicPlayers(matchSnapshot);
    }

    /**
     * Displays all info contained in matchSnapshot relative to the character the user is piloting (this includes
     * loaded and unloaded weapons, as well as the other cards currently in user's possession and the current damage
     * and mark count)
     * @param matchSnapshot Contains all the necessary info for a completed display of the current game state.
     */
    private static void displayPrivatePlayer(MatchSnapshot matchSnapshot) {
        display("You " + coloredString("[" + matchSnapshot.privatePlayerSnapshot.name + "-" + matchSnapshot.privatePlayerSnapshot.color + "]", matchSnapshot.privatePlayerSnapshot.color) + " have:");
        display("Loaded weapons:");
        if(matchSnapshot.privatePlayerSnapshot.getLoadedWeapons().isEmpty())
            display(NONE);
        for (int i = 0; i < matchSnapshot.privatePlayerSnapshot.getLoadedWeapons().size(); i++)
            display(matchSnapshot.privatePlayerSnapshot.getLoadedWeapons().get(i) + matchSnapshot.privatePlayerSnapshot.getLoadedWeaponsCost().get(i));
        display("Unloaded weapons:");
        if(matchSnapshot.privatePlayerSnapshot.getUnloadedWeapons().isEmpty())
            display(NONE);
        for (int i = 0; i < matchSnapshot.privatePlayerSnapshot.getUnloadedWeapons().size(); i++)
            display(matchSnapshot.privatePlayerSnapshot.getUnloadedWeapons().get(i) + matchSnapshot.privatePlayerSnapshot.getUnloadedWeaponsCost().get(i));
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

    /**
     * Displays all info contained in matchSnapshot relative to all the characters piloted by other players (this
     * includes unloaded weapons, as well as the current damage and mark count and how many powerup cards does
     * each player have)
     * @param matchSnapshot Contains all the necessary info for a completed display of the current game state.
     */
    private static void displayPublicPlayers(MatchSnapshot matchSnapshot) {
        for (int j = 0; j < matchSnapshot.getPublicPlayerSnapshot().size(); j++) {
            display(PLAYER + coloredString(matchSnapshot.getPublicPlayerSnapshot().get(j).name + "-" + matchSnapshot.getPublicPlayerSnapshot().get(j).color, matchSnapshot.getPublicPlayerSnapshot().get(j).color) + " has:");
            display("Loaded weapons: " + matchSnapshot.getPublicPlayerSnapshot().get(j).loadedWeaponsNumber);
            display("Unloaded weapons:");
            if(matchSnapshot.getPublicPlayerSnapshot().get(j).getUnloadedWeapons().isEmpty())
                display(NONE);
            for (int i = 0; i < matchSnapshot.getPublicPlayerSnapshot().get(j).getUnloadedWeapons().size(); i++)
                display(matchSnapshot.getPublicPlayerSnapshot().get(j).getUnloadedWeapons().get(i) + matchSnapshot.getPublicPlayerSnapshot().get(j).getUnloadedWeaponsCost().get(i));
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

    /**
     * Displays to the user every possible legal action their character could take, asking them to select one.
     * @param options List of possible legal actions
     */
    public static void displayActions(List<RemoteAction> options) {
        for(RemoteAction remoteAction : options){
            display(PRESS + remoteAction.index + " if you want to " + remoteAction.visualizable.description);
        }
    }

    /**
     * This method is called right after the user decides on what action the want to go for. It is used in case they
     * chose a targeting action (like firing or moving another player). It displays all the possible targets, relative to the previously selected action,
     * the user can choose.
     * @param data Parameter of the chosen RemoteAction, which contains all the possible targets the user can choose.
     */
    public static void displayTargets(RemoteAction.Data data) {
        int j = 0;
        for(String player : data.getPossiblePlayers())
            display(PRESS + j++ + " if you want to target player " + player);
        for(String square : data.getPossibleSquares())
            display(PRESS + j++ + " if you want to target square " + square);
        for(String pu : data.getPossiblePowerUps())
            display(PRESS + j++ + " if you want to use " + pu);
        for(String pu : data.getDiscardablePowerUps())
            display(PRESS + j++ + " if you want to discard " + pu);
        for(String ammo : data.getDiscardableAmmos())
            display(PRESS + j++ + " if you want to discard a " + ammo + " ammo");
        for(String weapon : data.getPossibleWeapons())
            display(PRESS + j++ + " if you want to reload the weapon" + weapon);
        if(data.canBeDone)
            display(PRESS + j + " if you want to confirm the action");
    }

    //------------------------------------------------------------------------------------------------------------------
    // DISPLAY MAP

    /**
     * Displays to the user all the info relative to the current map state (this includes every player's position on
     * the map, as well as what items and/or weapons are on each square).
     * @param matchSnapshot Contains all the necessary info for a completed display of the current game state.
     */
    private static void displayMap(MatchSnapshot matchSnapshot) {
        display("Map:", true);
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
                return BLANK + coloredString("[" + square.getNames().get(line) + "]", square.getColors().get(line))
                        + blanks(MAX_SIZE_SQUARE - square.getNames().get(line).length() - square.getCards().get(line).length() - costLength(square.getCardsCost().get(line)) - 6)
                        + "[" + square.getCards().get(line) + square.getCardsCost().get(line) + "]" + BLANK;
            if (square.getColors().size() > line && square.getCards().size() <= line)
                return BLANK + coloredString("[" + square.getNames().get(line) + "]", square.getColors().get(line))
                        + blanks(MAX_SIZE_SQUARE - square.getNames().get(line).length() - 3);
            if (square.getColors().size() <= line && square.getCards().size() > line)
                return blanks(MAX_SIZE_SQUARE - square.getCards().get(line).length() - costLength(square.getCardsCost().get(line)) - 3)
                        + "[" + square.getCards().get(line) + square.getCardsCost().get(line) + "]" + BLANK;
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

    private static int costLength(String cost) {
        if(cost.length() == 12)
            return 2;
        if(cost.length() == 19)
            return 4;
        return 0;
    }

    private static void displayKillShotTrack(List<List<String>> killShotTrack) {
        display("KillShot Track:", true);
        String line = TOP_LEFT + KILLSHOTTRACK_WALL;
        for(int i = 0; i < MAX_SKULLS - 1; i++)
            line = line.concat(TOP + KILLSHOTTRACK_WALL);
        line = line.concat(TOP_RIGHT);
        display(line);
        line = VERTICAL;
        for(int i = 0; i < MAX_SKULLS; i++)
            line = line.concat(blanks(9) + VERTICAL);
        display(line);
        String temp = VERTICAL;
        for (List<String> colors : killShotTrack)
            switch (colors.size()) {
                case 0:
                    temp = temp.concat(blanks(4) + "X" + blanks(4) + VERTICAL);
                    break;
                case 1:
                    temp = temp.concat(blanks(4) + coloredString(String.valueOf(colors.get(0).charAt(0)), colors.get(0)) + blanks(4) + VERTICAL);
                    break;
                case 2:
                    temp = temp.concat(blanks(3) + coloredString(String.valueOf(colors.get(0).charAt(0)), colors.get(0))
                            + BLANK + coloredString(String.valueOf(colors.get(1).charAt(0)), colors.get(1)) + blanks(3) + VERTICAL);
                    break;
                case 3:
                    temp = temp.concat(blanks(2) + coloredString(String.valueOf(colors.get(0).charAt(0)), colors.get(0))
                            + BLANK + coloredString(String.valueOf(colors.get(1).charAt(0)), colors.get(1))
                            + BLANK + coloredString(String.valueOf(colors.get(2).charAt(0)), colors.get(2)) + blanks(2) + VERTICAL);
                    break;
                default:
                    temp = temp.concat(BLANK);
                    int j;
                    for (j = 0; j < colors.size(); j++)
                        temp = temp.concat(coloredString(String.valueOf(colors.get(j).charAt(0)), colors.get(j)));
                    temp = temp.concat(blanks(MAX_SKULLS - j) + VERTICAL);
            }
        for(int i = killShotTrack.size(); i < MAX_SKULLS; i++)
            temp = temp.concat(blanks(9) + VERTICAL);
        display(temp);
        display(line);
        line = BOTTOM_LEFT + KILLSHOTTRACK_WALL;
        for(int i = 0; i < MAX_SKULLS - 1; i++)
            line = line.concat(BOTTOM + KILLSHOTTRACK_WALL);
        line = line.concat(BOTTOM_RIGHT);
        display(line);
    }

    //------------------------------------------------------------------------------------------------------------------
    //DISPLAY

    @SuppressWarnings("squid:S106")
    /**
     * Displays the string gotten as a  parameter using curColor has the text color
     * @param string Displayed String
     */
    private static void display(String string) {
        printStream.println(curColor + string);
    }

    /**
     * Displays the string gotten as parameter. Uses bold characters when the boolean parameter is true.
     * @param string Displayed String
     * @param bold Parameter used to indicate whether the String is going to be displayed in bold characters
     */
    private static void display(String string, boolean bold){
        if(bold)
            display("\u001B[1m" + string);
        else
            display(string);
    }

    /**
     * Returns the text written with the selected color
     * @param text String which needs to be returned
     * @param color String which represents the color used to write the "text" String
     * @return "text" String written in the selected color
     */
    private static String coloredString(String text, String color) {
        switch (color) {
            case "Blue":
                return ANSI_BLUE + text + ANSI_WHITE;
            case "Green":
                return  ANSI_GREEN + text + ANSI_WHITE;
            case "Grey":
                return  ANSI_GREY + text + ANSI_GREY; // Change to GREY
            case "Red":
                return ANSI_RED + text + ANSI_RED;
            case "Violet":
                return  ANSI_PURPLE + text + ANSI_WHITE;
            case "Yellow":
                return  ANSI_YELLOW + text + ANSI_WHITE;
            default:
                return ANSI_WHITE + text + ANSI_WHITE;
        }
    }
}
