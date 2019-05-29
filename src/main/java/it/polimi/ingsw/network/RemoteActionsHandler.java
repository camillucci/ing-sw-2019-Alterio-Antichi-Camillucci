package it.polimi.ingsw.network;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.action.Action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class RemoteActionsHandler implements IActionHandler {

    private final Player player;
    public final IEvent<RemoteActionsHandler, List<RemoteAction>> newActionsEvent = new Event<>();
    private List<Action> curActions;
    private Action selectedAction;

    public RemoteActionsHandler(Player player)
    {
        this.player = player;
    }

    protected abstract List<RemoteAction> createRemoteActions(List<Action> actions);

    public List<RemoteAction> getRemoteActions(Player player, List<Action> actions)
    {
        if(this.player == player)
            return createRemoteActions(curActions =  new ArrayList<>(actions));
        return Collections.emptyList();
    }

    public abstract void waitForClient() throws IOException;

    @Override
    public void chooseAction(int index) {
        selectedAction = curActions.get(index);
    }

    @Override
    public void doAction()
    {
        selectedAction.doAction();
    }

    @Override
    public void addPowerup(int index){
        this.selectedAction.usePowerUp(selectedAction.getPossiblePowerUps().get(index));
    }

    @Override
    public void addDiscardablePowerup(int index) {
        selectedAction.addDiscarded(selectedAction.getDiscardablePowerUps().get(index));
    }

    @Override
    public void addWeapon(int index) {
        selectedAction.addWeapon(player.getUnloadedWeapons().get(index));
    }

    @Override
    public void addTargetPlayer(int index)
    {
        selectedAction.addTarget(selectedAction.getPossiblePlayers().get(index));
    }

    @Override
    public void addTargetSquare(int index)
    {
        selectedAction.addTarget(selectedAction.getPossibleSquares().get(index));
    }

    @Override
    public List<String> getPossiblePlayers()
    {
        return selectedAction.getPossiblePlayers().stream().map(p -> p.name).collect(Collectors.toList());
    }

    @Override
    public List<String> getPossibleSquares()
    {
        return selectedAction.getPossibleSquares().stream().map(s -> s.name).collect(Collectors.toList());
    }

    @Override
    public List<String> getDiscardablePowerUps(){
        return selectedAction.getDiscardablePowerUps().stream().map(p -> p.name).collect(Collectors.toList());
    }

    @Override
    public List<String> getPossiblePowerups(){
        return selectedAction.getPossiblePowerUps().stream().map(p -> p.name).collect(Collectors.toList());
    }

    @Override
    public boolean canBeDone() {
        return selectedAction.canBeDone();
    }
}
