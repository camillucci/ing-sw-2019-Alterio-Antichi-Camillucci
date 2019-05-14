package it.polimi.ingsw.network;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.ActionsProvider;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.action.Action;

import java.util.List;
import java.util.stream.Collectors;

public abstract class RemoteActionsHandler implements IRemoteActionHandler {

    private final ActionsProvider provider;
    private final Player player;
    protected final IEvent<RemoteActionsHandler, List<RemoteAction>> newActionsEvent = new Event<>();
    private List<Action> curActions;
    private List<RemoteAction> curRemoteActions;
    protected Action selectedAction = null;

    public RemoteActionsHandler(ActionsProvider provider, Player player)
    {
        this.provider = provider;
        this.player = player;
        this.provider.newActionsEvent.addEventHandler(this::onNewActions);
    }

    private void onNewActions(Player player, List<Action> actions)
    {
        if(this.player != player)
            return;

        curActions = actions;
        curRemoteActions = createRemoteActions(actions);
        ((Event<RemoteActionsHandler, List<RemoteAction>>)newActionsEvent).invoke(this, curRemoteActions);
    }

    protected abstract List<RemoteAction> createRemoteActions(List<Action> actions);

    @Override
    public IEvent<RemoteActionsHandler, List<RemoteAction>> getNewActionsEvent()
    {
        return newActionsEvent;
    }

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
        return selectedAction.getPossibleSquares().stream().map(s -> s.getName()).collect(Collectors.toList());
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
