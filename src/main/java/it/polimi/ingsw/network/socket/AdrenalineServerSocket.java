package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.AdrenalineServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdrenalineServerSocket extends AdrenalineServer
{
    private TCPClient client;
    private RemoteActionsHandlerSocket remoteActionsHandler;
    private Logger logger = Logger.getLogger("adrenalineServerSocket");

    public AdrenalineServerSocket(TCPClient client, Controller controller)
    {
        super(controller);
        this.client = client;
    }

    public void start() {
        try {
            login();
        }
        catch(IOException | ClassNotFoundException e){
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    private void login() throws IOException, ClassNotFoundException {
        while(!setName(client.in().getObject())) // get name
            client.out().sendBool(false); // name not accepted
        client.out().sendBool(true); // name accepted
        client.out().sendObject(new ArrayList<>(availableColors()));
        int color = client.in().getInt(); //color chosen by user
        setColor(color);
        client.out().sendBool(isHost);
        if(isHost) {
            setGameLength(client.in().getInt()); //game length chosen by user
            setGameMap(client.in().getInt()); //map chosen by user
        }
        ready();
    }

    @Override
    protected void sendMessage(String message) throws IOException {
        client.out().sendObject(message);
    }

    @Override
    protected void notifyMatchStarted(MatchSnapshot matchSnapshot) throws IOException {
        sendMessage(MATCH_STARTED_MESSAGE);
        client.out().sendObject(matchSnapshot);
    }
    public static final String MATCH_STARTED_MESSAGE = "Match started\n";
}
