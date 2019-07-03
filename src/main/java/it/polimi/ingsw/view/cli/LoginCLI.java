package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.view.Login;

import java.io.IOException;
import java.util.List;

/**
 * Class used to manage all the initial interactions with the user, in case they chose a CLI based display model.
 */
public class LoginCLI extends Login {

    /**
     * Calls CLIMessenger's method to display the ask connection question. Makes sure that the answer is acceptable.
     * If it is, the event is invoked, otherwise the user is asked again.
     */
    @Override
    public void askConnection() throws IOException {
        CLIMessenger.askConnection();
        boolean connection;
        int choice = CLIParser.parser.parseChoice();
        connection = choice == 0;
        ((Event<Login, Boolean>)socketEvent).invoke(this, connection);
    }

    /**
     * Asks which color the user wants to take and sends the answer to the server via invoking the color Event.
     * @param availableColors
     */
    @Override
    public void notifyAvailableColor(List<String> availableColors) throws IOException {
        CLIMessenger.askColor(availableColors);
        ((Event<Login, Integer>)colorEvent).invoke(this, CLIParser.parser.parseIndex(availableColors.size()));
    }

    /**
     * If the user is the host, then two questions are asked about how they want to set up the game. Then the server
     * is notified of the user's answer via invoking gameLenghtevent and gameMapEvent
     * @param isHost Indicates whether the user is the host (first one to enter the room) or not
     */
    @Override
    public void notifyHost(boolean isHost) throws IOException  {
        if(isHost)
        {
            CLIMessenger.askGameLength();
            ((Event<Login, Integer>)gameLengthEvent).invoke(this, CLIParser.parser.parseGameLength());
            CLIMessenger.askGameMap();
            ((Event<Login, Integer>)gameMapEvent).invoke(this, CLIParser.parser.parseGameMap());
        }
    }

    /**
     * This method is called in case the user's username has already been taken by someone else. It calls the
     * askName method one more time if the accepted parameter is false.
     * @param accepted Indicates whether the user's name of choice has been taken or not
     */
    @Override
    public void notifyAccepted(boolean accepted) throws IOException {
        if(!accepted)
            askName();
    }

    /**
     * Calls the default intro methods. They display the intro script and ask the player for a username
     */
    @Override
    public void login() throws IOException {
        CLIMessenger.intro();
        CLIMessenger.login();
        askName();
    }

    /**
     * Asks the user to choose a name. Then the name is "filtered" by the parser and the server is notified via
     * invoking the nameEvent
     */
    private void askName() throws IOException {
        CLIMessenger.insertName();
        ((Event<Login, String>)nameEvent).invoke(this, CLIParser.parser.parseName());
    }

    /**
     * Prints a generic message as an output
     * @param message Displayed message
     */
    @Override
    public void onNewMessage(String message) {
        CLIMessenger.printMessage(message);
    }

    @Override
    public void disconnectedPlayerMessage(String name) {
        String message = name + " left the room";
        CLIMessenger.printMessage(message);
    }

    @Override
    public void newPlayerMessage(String name) {
        String message = name + " joined the room";
        CLIMessenger.printMessage(message);
    }

    @Override
    public void timerStartedMessage(int time) {
        String message = "Countdown is started:\n " + time + " seconds left";
        CLIMessenger.printMessage(message);
    }

    @Override
    public void timerTickMessage(int time) {
        String message = time + " seconds left";
        CLIMessenger.printMessage(message);
    }

    @Override
    public void reconnectedMessage(String name) {
        String message = "You're back!";
        CLIMessenger.printMessage(message);
    }

    @Override
    public void winnerMessage(String winner) {
        return;
    }

    @Override
    public void scoreboardMessage(String[][] scoreboard) {
        return;
    }

}
