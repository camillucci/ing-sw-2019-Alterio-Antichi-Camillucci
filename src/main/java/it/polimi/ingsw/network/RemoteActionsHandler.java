package it.polimi.ingsw.network;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.action.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RemoteActionsHandler
{
    public final IEvent<RemoteActionsHandler, RemoteAction.Data> actionDataRequired = new Event<>();
    private final Player player;
    private final List<Action> actions;
    private Action selectedAction;

    public RemoteActionsHandler(Player player, List<Action> actions)
    {
        this.player = player;
        this.actions = actions;
    }

    public List<RemoteAction> createRemoteActions()
    {
        List<RemoteAction> remoteActions = new ArrayList<>();
        for(int i=0; i < actions.size(); i++)
            remoteActions.add(new RemoteAction(i, actions.get(i).getVisualizable()));
        return remoteActions;
    }

    public void chooseAction(int index) {
        selectedAction = actions.get(index);
    }

    public void doAction()
    {
        selectedAction.doAction();
    }

    public void addPowerUp(int index){
        this.selectedAction.use(selectedAction.getPossiblePowerUps().get(index));
    }

    public void addDiscardedPowerUp(int index) {
        selectedAction.addPowerUp(selectedAction.getDiscardablePowerUps().get(index));
    }

    public void addWeapon(int index) {
        selectedAction.addWeapon(player.getUnloadedWeapons().get(index));
    }

    public void addDiscardedAmmo(int index) {
        selectedAction.discard(selectedAction.getDiscardableAmmos().get(index));
    }

    public void addTargetPlayer(int index)
    {
        selectedAction.addTarget(selectedAction.getPossiblePlayers().get(index));
    }

    public void addTargetSquare(int index)
    {
        selectedAction.addTarget(selectedAction.getPossibleSquares().get(index));
    }

    public List<String> getPossiblePlayers()
    {
        return selectedAction.getPossiblePlayers().stream().map(p -> p.name).collect(Collectors.toList());
    }

    public List<String> getPossibleSquares()
    {
        return selectedAction.getPossibleSquares().stream().map(s -> s.name).collect(Collectors.toList());
    }

    public List<String> getDiscardablePowerUps(){
        return selectedAction.getDiscardablePowerUps().stream().map(p -> p.name).collect(Collectors.toList());
    }

    public List<String> getPossiblePowerups(){
        return selectedAction.getPossiblePowerUps().stream().map(p -> p.name).collect(Collectors.toList());
    }

    public List<String> getPossibleWeapons()
    {
        return selectedAction.getPossibleWeapons().stream().map(w -> w.name).collect(Collectors.toList());
    }

    public List<String> getDiscardableAmmos(){
        return selectedAction.getDiscardableAmmos().stream().map(Ammo::getName).collect(Collectors.toList());
    }

    public boolean canBeDone() {
        return selectedAction.canBeDone();
    }

    public void askActionData() {
        ((Event<RemoteActionsHandler, RemoteAction.Data>)actionDataRequired).invoke(this, getActionData());
    }

    private RemoteAction.Data getActionData(){
        return new RemoteAction.Data(getPossiblePlayers(), getPossibleSquares(), getPossiblePowerups(), getDiscardablePowerUps(), getDiscardableAmmos(), getPossibleWeapons(), canBeDone());
    }
}
