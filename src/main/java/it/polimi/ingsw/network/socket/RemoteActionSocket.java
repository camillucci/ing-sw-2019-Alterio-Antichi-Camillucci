package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;
import it.polimi.ingsw.network.RemoteAction;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RemoteActionSocket extends RemoteAction implements Serializable
{
    private int index;
    private transient TCPClient server;

    public RemoteActionSocket(int index){
        super(index);
        this.index = index;
    }

    public void initialize(TCPClient server) throws IOException
    {
        this.server = server;
        server.out().sendInt(this.index);
    }

    @Override
    public void addTargetPlayer(String target) throws IOException
    {
        server.out().sendInt(RemoteActionsHandlerSocket.ADD_PLAYER);
        server.out().sendInt(possiblePlayers.indexOf(target));
    }

    @Override
    public void addTargetSquare(String target) throws IOException
    {
        server.out().sendInt(RemoteActionsHandlerSocket.ADD_SQUARE);
        server.out().sendInt(possibleSquares.indexOf(target));
    }

    @Override
    public void usePowerUp(String powerUp) throws IOException {
        server.out().sendInt(RemoteActionsHandlerSocket.ADD_POWERUP);
        server.out().sendInt(discardablePowerUps.indexOf(powerUp));
    }

    @Override
    public void addDiscardable(String powerUp) throws IOException {
        server.out().sendInt(RemoteActionsHandlerSocket.ADD_DISCARDABLE);
        server.out().sendInt(discardablePowerUps.indexOf(powerUp));
    }

    @Override
    public void addWeapon(String weapon) throws IOException {
        server.out().sendInt(RemoteActionsHandlerSocket.ADD_WEAPON);
        server.out().sendInt(ownerPlayer.getUnloadedWeapons().indexOf(weapon));
    }

    @Override
    public void doAction() throws IOException
    {
        server.out().sendInt(RemoteActionsHandlerSocket.DO_ACTION);
    }

    @Override
    public List<String> getPossiblePlayers() throws IOException, ClassNotFoundException
    {
        server.out().sendInt(RemoteActionsHandlerSocket.GET_PLAYERS);
        return new ArrayList<>(possiblePlayers = server.in().getObject());
    }

    @Override
    public List<String> getPossibleSquares() throws IOException, ClassNotFoundException
    {
        server.out().sendInt(RemoteActionsHandlerSocket.GET_SQUARES);
        return new ArrayList<>(possibleSquares = server.in().getObject());
    }

    @Override
    public List<String> getPossiblePowerups() throws IOException, ClassNotFoundException {
        server.out().sendInt(RemoteActionsHandlerSocket.GET_POWERUPS);
        return new ArrayList<>(possiblePowerUps = server.in().getObject());
    }

    @Override
    public List<String> getDiscardablePowerups() throws IOException, ClassNotFoundException {
        server.out().sendInt(RemoteActionsHandlerSocket.GET_DISCARDABLES);
        return new ArrayList<>(discardablePowerUps = server.in().getObject());
    }

    @Override
    public boolean canBeDone() throws IOException {
        server.out().sendInt(RemoteActionsHandlerSocket.CAN_BE_DONE);
        return server.in().getBool();
    }

    @Override
    public String toString() {
        return "Azione";
    }
}
