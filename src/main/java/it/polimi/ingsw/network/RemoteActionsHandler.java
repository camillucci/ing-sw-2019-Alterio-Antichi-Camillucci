package it.polimi.ingsw.network;

import it.polimi.ingsw.model.ActionsProvider;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class RemoteActionsHandler
{
    public static final int DO_ACTION = 0;
    public static final int ADD_PLAYER = 1;
    public static final int ADD_SQUARE = 2;
    public static final int GET_PLAYERS = 3;
    public static final int GET_SQUARES = 4;

    ActionsProvider provider;
    Client client;

    public RemoteActionsHandler(ActionsProvider provider, Client client)
    {
        this.provider = provider;
    }

    private void getChoice() throws Exception
    {
        try
        {
            boolean stop = false;
            do
            {
                sendActions();
                handleAction(provider.getActions().get(client.in().getInt()));
            }while(!stop);
        }
        catch(Exception e) {}
    }

    private void handleAction(Action action) throws Exception
    {
        boolean completed = false;
        while(!completed)
            switch ((client.in().getInt()))
            {
                case DO_ACTION:
                    action.doAction();
                    completed = true;
                    break;
                case ADD_PLAYER:
                    action.addTarget(action.getPossiblePlayers().get(client.in().getInt()));
                    break;
                case ADD_SQUARE:
                    action.addTarget(action.getPossibleSquares().get(client.in().getInt()));
                    break;
                case GET_PLAYERS:
                {
                    ArrayList<PublicPlayerSnapshot> ret = new ArrayList<>();
                    for(Player p: action.getPossiblePlayers())
                        ret.add(new PublicPlayerSnapshot(p));
                    client.out().sendObject(ret);
                    break;
                }
                case GET_SQUARES:
                {
                    ArrayList<SquareSnapshot> ret = new ArrayList<>();
                    for (Square s : action.getPossibleSquares())
                        ret.add(new SquareSnapshot(s));
                    client.out().sendObject(ret);
                    break;
                }
                default:
                    throw new IllegalStateException("Unexpected value: " + "oluc oissela");
            }
    }

    private void sendActions()
    {
        //todo
    }

}
