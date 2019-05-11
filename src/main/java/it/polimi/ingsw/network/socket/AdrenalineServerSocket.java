package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.AdrenalineServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdrenalineServerSocket extends AdrenalineServer
{
    private TCPClient client;
    private TCPRemoteActionsHandler remoteActionsHandler;

    protected AdrenalineServerSocket(Controller controller, TCPClient client)
    {
        super(controller);
        this.client = client;
    }

    public void login() throws Exception
    {
        setInterface(client.in().getBool());
        while(!setName(client.in().getObject())) // get name
            client.out().sendBool(false); // name not accepted
        client.out().sendBool(true); // name accepted
        client.out().sendObject(new ArrayList<>(availableColors()));
        int color = client.in().getObject(); //color chosen by user
        setColor(color);
        boolean isHost = joinRoom();
        client.out().sendBool(isHost);
        if(isHost) {
            setGameLength(client.in().getInt()); //game length chosen by user
            setGameMap(client.in().getInt()); //map chosen by user
            setGameLength(gameLength);
            setGameMap(gameMap);
        }
    }

    //spawn should probably be included in the parts of the game managed by actions.

    public int spawn(List<String> powerupCards) throws IOException {
        client.out().sendObject(new ArrayList<>(powerupCards));
        return client.in().getInt();
    }

    @Override
    public void updateView(MatchSnapshot matchSnapshot) throws IOException {
        client.out().sendObject(matchSnapshot);
    }
}
