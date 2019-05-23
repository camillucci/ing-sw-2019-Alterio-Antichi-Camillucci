package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.ActionsProvider;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.network.RemoteActionsHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RemoteActionsHandlerSocket extends RemoteActionsHandler
{
    public static final int GET_PLAYERS = 0;
    public static final int GET_SQUARES = 1;
    public static final int GET_POWERUPS = 2;
    public static final int GET_DISCARDABLES = 3;
    public static final int ADD_PLAYER = 4;
    public static final int ADD_SQUARE = 5;
    public static final int ADD_POWERUP = 6;
    public static final int ADD_DISCARDABLE = 7;
    public static final int ADD_WEAPON = 8;
    public static final int CAN_BE_DONE = 9;
    public static final int DO_ACTION = 10;

    TCPClient client;
    Logger logger = Logger.getLogger("remoteActionsHandlerSocket");

    public RemoteActionsHandlerSocket(ActionsProvider provider, Player player, TCPClient client)
    {
        super(provider,player);
        this.client = client;
        newActionsEvent.addEventHandler((a, actions) -> sendActions(actions));
    }

    private void getChoice() throws Exception
    {
        try
        {
            boolean stop = false;
            do
            {
                chooseAction(client.in().getInt());
                handleAction();
            }while(!stop);
        }
        catch(Exception e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    private void handleAction() throws IOException
    {
        boolean completed = false;
        while(!completed)
            switch (client.in().getInt())
            {
                case GET_PLAYERS:
                    client.out().sendObject(new ArrayList<>(getPossiblePlayers()));
                    break;
                case GET_SQUARES:
                    client.out().sendObject(new ArrayList<>(getPossibleSquares()));
                    break;
                case GET_POWERUPS:
                    client.out().sendObject(new ArrayList<>(getPossiblePowerups()));
                    break;
                case GET_DISCARDABLES:
                    client.out().sendObject(new ArrayList<>(getDiscardablePowerUps()));
                    break;
                case ADD_PLAYER:
                    addTargetPlayer(client.in().getInt());
                    break;
                case ADD_SQUARE:
                    addTargetSquare(client.in().getInt());
                    break;
                case ADD_POWERUP:
                    addPowerup(client.in().getInt());
                    break;
                case ADD_DISCARDABLE:
                    addDiscardablePowerup(client.in().getInt());
                    break;
                case ADD_WEAPON:
                    addWeapon(client.in().getInt());
                    break;
                case CAN_BE_DONE:
                    client.out().sendBool(canBeDone());
                    break;
                case DO_ACTION:
                    doAction();
                    completed = true;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + "oluc oissela");
            }
    }

    private void sendActions(List<RemoteAction> remoteActions)
    {
        try
        {
            for(RemoteAction a : remoteActions)
                client.out().sendObject(a);
        }
        catch(IOException e)
        {
            //todo
        }
    }

    @Override
    protected List<RemoteAction> createRemoteActions(List<Action> actions) {
        List<RemoteAction> ret = new ArrayList<>();
        for(int i=0; i < actions.size(); i++)
            ret.add(new RemoteActionSocket(i));
        return ret;
    }
}
