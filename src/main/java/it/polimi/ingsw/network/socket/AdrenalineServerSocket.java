package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.AdrenalineServer;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.generics.Utils.tryDo;

public class AdrenalineServerSocket extends AdrenalineServer
{
    private TCPClient client;
    private RemoteActionsHandlerSocket remoteActionsHandler;

    public AdrenalineServerSocket(TCPClient client)
    {
        this.client = client;
    }

    public void start() {
        try {
            login();
        }
        catch(IOException e){}
        catch (ClassNotFoundException e){}
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
        joinedRoom.notifyPlayerReady();
    }

    @Override
    protected void sendMessage(String message) throws IOException {
        client.out().sendObject(message);
    }
}
