package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.view.Login;

import java.io.IOException;
import java.util.List;

public class LoginCLI extends Login {

    @Override
    public void notifyAvailableColor(List<String> availableColors) throws IOException {
        CLIMessenger.askColor(availableColors);
        ((Event<Login, Integer>)colorEvent).invoke(this, CLIParser.parseIndex(availableColors.size()));
    }

    @Override
    public void notifyHost(boolean isHost) throws IOException  {
        if(isHost)
        {
            CLIMessenger.askGameLenght();
            ((Event<Login, Integer>)gameLengthEvent).invoke(this, CLIParser.parseIndex(13));
            CLIMessenger.askGameMap();
            ((Event<Login, Integer>)gameMapEvent).invoke(this, CLIParser.parseIndex(13));
        }
    }

    @Override
    public void notifyAccepted(boolean accepted) throws IOException {
        if(!accepted)
            askName();
    }


    @Override
    public void notifyMatchStart() {
        //todo
    }

    @Override
    public void login() throws IOException {
        askName();
    }

    private void askName() throws IOException {
        CLIMessenger.insertName();
        ((Event<Login, String>)nameEvent).invoke(this, CLIParser.parseName());
    }

    @Override
    public void onNewMessage(String message) {
        CLIMessenger.printMessage(message);
    }
}
