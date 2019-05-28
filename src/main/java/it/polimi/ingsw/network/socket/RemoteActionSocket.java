package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.network.RemoteAction;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RemoteActionSocket extends RemoteAction implements Serializable
{
    private int socketIndex;
    private transient TCPClient server;

    public RemoteActionSocket(int socketIndex){
        super(socketIndex);
        this.socketIndex = socketIndex;
    }

    public void initialize(TCPClient server) throws IOException
    {
        this.server = server;
        server.out().sendInt(this.socketIndex);
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
    public void addDiscardableAmmo(String ammo) throws IOException {
        //TODO
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
        possiblePlayers = server.in().getObject();
        return new ArrayList<>(possiblePlayers);
    }

    @Override
    public List<String> getPossibleSquares() throws IOException, ClassNotFoundException
    {
        server.out().sendInt(RemoteActionsHandlerSocket.GET_SQUARES);
        possibleSquares = server.in().getObject();
        return new ArrayList<>(possibleSquares);
    }

    @Override
    public List<String> getPossiblePowerups() throws IOException, ClassNotFoundException {
        server.out().sendInt(RemoteActionsHandlerSocket.GET_POWERUPS);
        possiblePowerUps = server.in().getObject();
        return new ArrayList<>(possiblePowerUps);
    }

    @Override
    public List<String> getDiscardablePowerups() throws IOException, ClassNotFoundException {
        server.out().sendInt(RemoteActionsHandlerSocket.GET_DISCARDABLES);
        discardablePowerUps = server.in().getObject();
        return new ArrayList<>(discardablePowerUps);
    }

    @Override
    public List<String> getDiscardableAmmos() throws IOException, ClassNotFoundException {
        //TODO
        return null;
    }

    @Override
    public List<String> getPossibleWeapons() throws IOException, ClassNotFoundException {
        //TODO
        return null;
    }

    @Override
    public boolean canBeDone() throws IOException {
        server.out().sendInt(RemoteActionsHandlerSocket.CAN_BE_DONE);
        return server.in().getBool();
    }

    @Override
    public String toString() {
        return "Action";
    }
}
