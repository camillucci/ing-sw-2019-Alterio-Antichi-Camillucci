package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.view.Login;

import java.io.IOException;
import java.util.List;

public class LoginCLI extends Login {
    @Override
    public void askConnection() throws IOException {
        CLIMessenger.askConnection();
        boolean connection;
        int choice = CLIParser.parser.parseChoice();
        connection = choice == 0;
        ((Event<Login, Boolean>)socketEvent).invoke(this, connection);
    }

    @Override
    public void notifyAvailableColor(List<String> availableColors) throws IOException {
        CLIMessenger.askColor(availableColors);
        ((Event<Login, Integer>)colorEvent).invoke(this, CLIParser.parser.parseIndex(availableColors.size()));
    }

    @Override
    public void notifyHost(boolean isHost) throws IOException  {
        if(isHost)
        {
            CLIMessenger.askGameLenght();
            ((Event<Login, Integer>)gameLengthEvent).invoke(this, CLIParser.parser.parseGameLength());
            CLIMessenger.askGameMap();
            ((Event<Login, Integer>)gameMapEvent).invoke(this, CLIParser.parser.parseGameMap());
        }
    }

    @Override
    public void notifyAccepted(boolean accepted) throws IOException {
        if(!accepted)
            askName();
    }

    @Override
    public void login() throws IOException {
        CLIMessenger.intro();
        CLIMessenger.login();
        askName();
    }

    private void askName() throws IOException {
        CLIMessenger.insertName();
        ((Event<Login, String>)nameEvent).invoke(this, CLIParser.parser.parseName());
    }

    @Override
    public void onNewMessage(String message) {
        CLIMessenger.printMessage(message);
    }

    @Override
    public void onModelChanged(MatchSnapshot matchSnapshot) {
        // no way to go here
    }
}
