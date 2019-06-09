package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.AdrenalineServer;
import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdrenalineServerSocket extends AdrenalineServer
{
    private TCPClient client;
    public AdrenalineServerSocket(TCPClient client, Controller controller)
    {
        super(controller);
        this.client = client;
        startPinging();
    }

    public void start() {
        bottleneck.tryDo(this::waitForCommand);
    }
    private void waitForCommand() throws IOException, ClassNotFoundException {
        while(true)
            newServerCommand(client.in().getObject());
    }

    @Override
    protected void startPinging() {
        client.startPinging(PING_PERIOD, this::onExceptionGenerated);
    }

    @Override
    protected void stopPinging() {
        client.stopPinging();
    }

    @Override
    protected void sendCommand(Command<View> command) throws IOException {
        client.out().sendObject(command);
    }
}
