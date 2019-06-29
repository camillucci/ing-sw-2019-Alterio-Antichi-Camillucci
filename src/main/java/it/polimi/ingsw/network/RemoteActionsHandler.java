package it.polimi.ingsw.network;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.action.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used to handle user's decision about which actions they want to go for. This class is placed in
 * server's side and has public methods that can be called from the client (in remote) in order to communicate the
 * player's decisions.
 */
public class RemoteActionsHandler
{
    /**
     * Event other classes can subscribe to. When this event is invoked, it notifies every subscriber. This event is
     * invoked when the askActionData method is called.
     */
    public final IEvent<RemoteActionsHandler, RemoteAction.Data> actionDataRequired = new Event<>();

    /**
     * Reference to the player the list of remote actions is associated with
     */
    public final Player player;

    /**
     * List of actions that can be selected and executed if the client communicates to.
     */
    private final List<Action> actions;

    /**
     * Action that as been selected from the user and is ready to be executed
     */
    private Action selectedAction;

    /**
     * Constructor that assigns the input parameters to their global correspondences
     * @param player Reference to the player the list of remote actions is associated with
     * @param actions List of actions that can be selected and executed if the client communicates to.
     */
    public RemoteActionsHandler(Player player, List<Action> actions)
    {
        this.player = player;
        this.actions = actions;
    }

    /**
     * Creates a list of remote actions based on the actions list in the global parameters section
     * @return The list of remote actions newly created
     */
    public List<RemoteAction> createRemoteActions()
    {
        List<RemoteAction> remoteActions = new ArrayList<>();
        for(int i=0; i < actions.size(); i++)
            remoteActions.add(new RemoteAction(i, actions.get(i).getVisualizable()));
        return remoteActions;
    }

    /**
     * Based on the input index, this method calculates which action it corresponds to and then proceeds to make
     * said action the selected one.
     * @param index Integer representing the position of the selected action in the list of possible actions.
     */
    public void chooseAction(int index) {
        selectedAction = actions.get(index);
    }

    /**
     * Calls the doAction method on the action that's been previously selected
     */
    public void doAction()
    {
        selectedAction.doAction();
    }

    /**
     * Uses the power up card associated with the index, by first getting all the power ups relative to the
     * selected action and then finding the one that occupies the "index" position on the list.
     * @param index Integer that represents the position of the selected power up card on the list.
     */
    public void addPowerUp(int index){
        this.selectedAction.use(selectedAction.getPossiblePowerUps().get(index));
    }

    public void addDiscardedPowerUp(int index) {
        selectedAction.addPowerUp(selectedAction.getDiscardablePowerUps().get(index));
    }

    /**
     * Adds the weapon card associated with the index, by first getting all the weapons relative to the
     * selected action and then finding the one that occupies the "index" position on the list.
     * @param index Integer that represents the position of the selected weapon card on the list.
     */
    public void addWeapon(int index) {
        selectedAction.addWeapon(player.getUnloadedWeapons().get(index));
    }

    /**
     * Discards the ammo card associated with the index, by first getting all the ammo cards relative to the
     * selected action and then finding the one that occupies the "index" position on the list.
     * @param index Integer that represents the position of the selected ammo card on the list.
     */
    public void addDiscardedAmmo(int index) {
        selectedAction.discard(selectedAction.getDiscardableAmmos().get(index));
    }

    /**
     * Adds the player associated with the index to the targets list, by first getting all the possible target
     * players associated with the selected action and then finding the one that occupies the "index" position on the
     * list.
     * @param index Integer that represents the position of the selected target player on the list.
     */
    public void addTargetPlayer(int index)
    {
        selectedAction.addTarget(selectedAction.getPossiblePlayers().get(index));
    }

    /**
     * Adds the square associated with the index to the targets list, by first getting all the possible target
     * squares associated with the selected action and then finding the one that occupies the "index" position on the
     * list.
     * @param index Integer that represents the position of the selected target square on the list.
     */
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

    /**
     * Invokes the actionDataRequired event.
     */
    public void askActionData() {
        ((Event<RemoteActionsHandler, RemoteAction.Data>)actionDataRequired).invoke(this, getActionData());
    }

    private RemoteAction.Data getActionData(){
        return new RemoteAction.Data(getPossiblePlayers(), getPossibleSquares(), getPossiblePowerups(), getDiscardablePowerUps(), getDiscardableAmmos(), getPossibleWeapons(), canBeDone());
    }

    public void rollback() {
        selectedAction = actions.get(actions.size() - 1);
    }
}
