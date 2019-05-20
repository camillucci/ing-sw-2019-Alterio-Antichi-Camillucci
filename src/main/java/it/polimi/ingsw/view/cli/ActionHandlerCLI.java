package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.network.socket.RemoteActionSocket;
import it.polimi.ingsw.view.ActionHandler;
import it.polimi.ingsw.view.Login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActionHandlerCLI extends ActionHandler {
    @Override
    public void chooseAction(List<RemoteAction> options) throws IOException {
        CLIMessenger.displayActions((ArrayList<RemoteAction>) options);
        ((Event<ActionHandler, Integer>)choiceEvent).invoke(this, CLIParser.parser.parseIndex(options.size()));
    }

    @Override
    public void onNewMessage(String message) {
        //TODO
    }

    @Override
    public void onModelChanged(MatchSnapshot matchSnapshot) {
        //TODO
    }
}
