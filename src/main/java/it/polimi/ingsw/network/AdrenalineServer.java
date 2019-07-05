package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.Room;
import it.polimi.ingsw.generics.Bottleneck;
import it.polimi.ingsw.snapshots.MatchSnapshot;
import it.polimi.ingsw.view.View;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains all the info and methods that concern the server side, but are generic enough to be independent
 * from the type of connection that the user has chosen
 */
public abstract class AdrenalineServer implements IAdrenalineServer
{
    /**
     * reference to the controller the class is going to communicate with. This parameter is final given that there is
     * only one controller.
     */
    private final Controller controller;

    /**
     * Reference to the room this class is associated and communicates with.
     */
    private Room joinedRoom;

    /**
     * String that represents the name of the player that this class is associated with.
     */
    protected String name;

    /**
     * Color chosen by the player associated with this class
     */
    private List<String> availableColors;
    private List<String> otherPlayers = new ArrayList<>();
    protected Bottleneck bottleneck = new Bottleneck();
    protected RemoteActionsHandler remoteActionsHandler;
    private BiConsumer<Room, Integer> onEndMatchEvent = (a, b) ->
    {
        this.close();
        removeEvents();
    };
    private BiConsumer<Room, String[][]> scoreEventHandler = (a, scoreBoard) -> bottleneck.tryDo( () -> scoreboardMessage(scoreBoard));

    private BiConsumer<Room, String> winnerEventHandler = (a, winner) -> bottleneck.tryDo( () -> winnerMessage(winner));
    private BiConsumer<Room, Integer> timerStartEventHandler = (a, timeout) -> bottleneck.tryDo( () -> timerStartedMessage(timeout));
    private BiConsumer<Room, Integer> timerTickEventHandler = (a, timeLeft) -> bottleneck.tryDo( () -> timerTickMessage(timeLeft));
    private BiConsumer<Room, Integer> timerStopEventHandler = (a, timeLeft) -> bottleneck.tryDo( () -> sendMessage(TIMER_STOPPED_MESSAGE));
    private BiConsumer<Room, String> reconnectedEventHandler = (a, name) -> bottleneck.tryDo( () -> reconnectedMessage(name));
    private BiConsumer<Room, String> newPlayerEventHandler = (a, name) -> notifyPlayer(name);
    private BiConsumer<Room, String> playerDisconnectedEventHandler = (a, name) -> notifyPlayerDisconnected(name);
    private BiConsumer<Room, Room.ModelEventArgs> modelUpdatedEventHandler = (a, model) -> bottleneck.tryDo( () -> onModelUpdated(model));
    private BiConsumer<Room, String> turnTimeoutEventHandler = (a, name) -> bottleneck.tryDo( () -> onTurnTimeout(name));
    protected volatile boolean isDisconnected = false;

    private void onTurnTimeout(String name) throws IOException
    {
        if(!name.equals(this.name))
            return;
        remoteActionsHandler = null;

        List<RemoteAction> emptyList = new ArrayList<>();
        sendCommand(new Command<>(view -> view.getActionHandler().chooseAction(emptyList)));
    }

    protected Logger logger = Logger.getLogger("AdrenalineServer");

    public abstract void close();

    private boolean newTurn = true;

    @Override
    public void newActionCommand(Command<RemoteActionsHandler> command)
    {
        try {
            joinedRoom.setClientIdle(name, true);
            if(remoteActionsHandler != null)
                bottleneck.tryDo( () -> command.invoke(this.remoteActionsHandler));
        } catch (Room.TurnTimeoutException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }
    /**
     * Constructor. It assigns the input reference to the controller global parameter and subscribes to the
     * bottleneck.exceptionGenerated event.
     * @param controller
     */
    public AdrenalineServer(Controller controller){
        this.controller = controller;
        bottleneck.exceptionGenerated.addEventHandler((a, exception) -> onExceptionGenerated(exception));
    }

    protected void onModelUpdated(Room.ModelEventArgs model) throws IOException {
        if(model.playerName.equals(this.name))
        {
            MatchSnapshot matchSnapshot = model.matchSnapshot;
            sendCommand(new Command<>(view -> view.getCurViewElement().onModelChanged(matchSnapshot)));
            this.remoteActionsHandler = model.actionsHandler;
            remoteActionsHandler.actionDataRequired.addEventHandler((a, data) -> bottleneck.tryDo( () -> sendCommand(new Command<>(view -> view.getActionHandler().updateActionData(data)))));
            List<RemoteAction> remoteActions = remoteActionsHandler.createRemoteActions();
            if(!remoteActions.isEmpty() && model.actionsHandler.player.name.equals(this.name))
            {
                if(newTurn) {
                    sendCommand(new Command<>(view -> view.getActionHandler().onTurnStart()));
                    newTurn = false;
                }
                sendCommand(new Command<>(view -> view.getActionHandler().chooseAction(remoteActions)));
            }
            else
                newTurn = true;
        }
    }

    public void newServerCommand(Command<IAdrenalineServer> command) {
        bottleneck.tryDo(() -> command.invoke(this));
    }

    /**
     * Method called when a connection exception comes up. The class unsubscribes to the events and notifies the
     * controller about the newly occurred player's disconnection.
     */
    protected void onExceptionGenerated(Exception e){
        if(isDisconnected)
            return;
        isDisconnected = true;
        removeEvents();
        controller.notifyPlayerDisconnected(name);
    }

    /**
     * This method notifies the client about the disconnection of the player with the same name as the one represented
     * by the input String
     * @param name String that represents the name of the disconnected player
     */
    private synchronized void notifyPlayerDisconnected(String name){
        if(name.equals(this.name))
            return;
        otherPlayers.remove(name);
        bottleneck.tryDo(() -> playerDisconnectedMessage(name));
    }

    /**
     * Checks whether the name chosen by the user is available by communicating with the controller. If it is, it
     * proceeds to ask what the available colors are and send them to the client. If it isn't, the client is notified
     * that the chosen name is not available.
     * @param name Name chosen by the user
     */
    @Override
    public void setName(String name) throws IOException {
        Room tmp = controller.checkReconnected(name);
        if(tmp != null) {
            joinedRoom = tmp;
            joinedRoom.reconnect(name);
            setupRoomEvents();
            this.name = name;
            otherPlayers = joinedRoom.getOtherPlayers(name);
            reconnectedMessage(name);
            onModelUpdated(joinedRoom.getCurModel(name));

            List<String> disconnected = joinedRoom.getDisconnectedPlayers();
            sendCommand(new Command<>(view ->
            {
                for(String p : disconnected)
                    view.getActionHandler().disconnectedPlayerMessage(p);
            }));
        }
        else if(controller.newPlayer(name)) {
            this.name = name;
            List<String> colors = availableColors();
            sendCommand(new Command<>(view -> {
                view.getLogin().notifyAccepted(true);
                view.getLogin().notifyAvailableColor(colors);
            }));
        }
        else
            sendCommand(new Command<>( view -> view.getLogin().notifyAccepted(false)));
    }

    /**
     * Asks the controller for an available room and gets the colors available in said room.
     * @return The available colors in the joined room.
     */
    @Override
    public List<String> availableColors()
    {
        try {
            joinedRoom = controller.getAvailableRoom();
            availableColors = joinedRoom.getAvailableColors();
        }
        catch (Room.MatchStartingException e) {
            return availableColors();
        }
        return availableColors;
    }

    /**
     * Adds the player to the room by communicating their username and the chosen color to the controller.
     * @param colorIndex Index of the color that has been chosen by the user
     */
    @Override
    public void setColor(int colorIndex) throws IOException {
        try
        {
            joinedRoom.addPlayer(availableColors.get(colorIndex), name);
            notifyIsHost();
        }
        catch (Room.MatchStartingException | Room.NotAvailableColorException e)
        {
            List<String> colors = availableColors();
            sendCommand(new Command<>(view -> view.getLogin().notifyAvailableColor(colors)));
        }
    }

    /**
     * Checks whether the user associated to this class is the host of the room by communicating their name to the
     * controller. If they are, the client is notified. Otherwise the user is put on ready status.
     */
    private void notifyIsHost() throws IOException {
        boolean isHost = joinedRoom.isHost(name);
        sendCommand(new Command<>(view -> view.getLogin().notifyHost(isHost)));
        if(!isHost)
            ready();
    }

    /**
     * Communicates host's decision about the number of skulls to the controller
     * @param gameLength Number of skulls that have been selected by the host
     */
    @Override
    public void setGameLength(int gameLength) {
        joinedRoom.setGameLength(gameLength);
    }

    /**
     * Communicates host's decision about the type of map to the controller
     * @param choice Type of the map that has been selected by the host
     */
    @Override
    public void setGameMap(int choice) {
        joinedRoom.setGameSize(choice);
        ready();
    }

    private synchronized void notifyPlayer(String name){
        if(otherPlayers.contains(name))
            return;
        if(this.name != name)
            otherPlayers.add(name);
        bottleneck.tryDo(() -> newPlayerMessage(name));
    }


    @Override
    public void ready() {
        setupRoomEvents();
        for(String name : joinedRoom.getPlayerNames())
            notifyPlayer(name);
        joinedRoom.notifyPlayerReady(name);
    }

    protected abstract void sendCommand(Command<View> command) throws IOException;

    protected void sendMessage(String message) throws IOException
    {
        sendCommand(new Command<>(v -> v.getCurViewElement().onNewMessage(message)));
    }

    protected void scoreboardMessage(String[][] scoreboard) throws IOException {
        sendCommand(new Command<>(v -> v.getActionHandler().scoreboardMessage(scoreboard)));
    }

    protected void winnerMessage(String winner) throws IOException {
        sendCommand(new Command<>(v -> v.getActionHandler().winnerMessage(winner)));
    }

    protected void timerStartedMessage(int time) throws IOException {
        sendCommand(new Command<>(v -> v.getLogin().timerStartedMessage(time)));
    }

    protected void timerTickMessage (int time) throws IOException {
        sendCommand(new Command<>(v -> v.getLogin().timerTickMessage(time)));
    }

    protected void playerDisconnectedMessage (String name) throws IOException {
        sendCommand(new Command<>(v -> v.getCurViewElement().disconnectedPlayerMessage(name)));
    }

    protected void newPlayerMessage (String name) throws IOException {
        sendCommand(new Command<>(v -> v.getLogin().newPlayerMessage(name)));
    }

    protected void reconnectedMessage (String name) throws IOException {
        sendCommand(new Command<>(v -> v.getActionHandler().reconnectedMessage(name)));
    }

    /**
     * Checks whether a room has been joined already. If it is, this method unsubscribes from every event that this
     * class was subscribed to via the setupRoomEvents method. Also, if the match has already started, this method
     * unsubscribes the class from the modelUpdate event.
     */
    private void removeEvents() {
        if(joinedRoom != null) {
            joinedRoom.reconnectedPlayerEvent.removeEventHandler(reconnectedEventHandler);
            joinedRoom.winnerEvent.removeEventHandler(winnerEventHandler);
            joinedRoom.scoreEvent.removeEventHandler(scoreEventHandler);
            joinedRoom.timerStartEvent.removeEventHandler(timerStartEventHandler);
            joinedRoom.timerTickEvent.removeEventHandler(timerTickEventHandler);
            joinedRoom.timerStopEvent.removeEventHandler(timerStopEventHandler);
            joinedRoom.newPlayerEvent.removeEventHandler(newPlayerEventHandler);
            joinedRoom.playerDisconnectedEvent.removeEventHandler(playerDisconnectedEventHandler);
            joinedRoom.turnTimeoutEvent.removeEventHandler(turnTimeoutEventHandler);
            joinedRoom.modelUpdatedEvent.removeEventHandler(modelUpdatedEventHandler);
            joinedRoom.endMatchEvent.removeEventHandler(onEndMatchEvent);
        }
    }

    /**
     * Subscribes to all the events the server is interested in
     */
    private void setupRoomEvents()
    {
        joinedRoom.reconnectedPlayerEvent.addEventHandler(reconnectedEventHandler);
        joinedRoom.winnerEvent.addEventHandler(winnerEventHandler);
        joinedRoom.scoreEvent.addEventHandler(scoreEventHandler);
        joinedRoom.timerStartEvent.addEventHandler(timerStartEventHandler);
        joinedRoom.timerTickEvent.addEventHandler(timerTickEventHandler);
        joinedRoom.timerStopEvent.addEventHandler(timerStopEventHandler);
        joinedRoom.newPlayerEvent.addEventHandler(newPlayerEventHandler);
        joinedRoom.playerDisconnectedEvent.addEventHandler(playerDisconnectedEventHandler);
        joinedRoom.turnTimeoutEvent.addEventHandler(turnTimeoutEventHandler);
        joinedRoom.modelUpdatedEvent.addEventHandler(modelUpdatedEventHandler);
        joinedRoom.endMatchEvent.addEventHandler(onEndMatchEvent);
    }

    /**
     * Default string that represents th starting match message
     */
    private static final String MATCH_STARTING_MESSAGE = "Match is starting";

    /**
     * String that represents the default message sent to the user when the countdown is interrupted before the match
     * starts.
     */
    private static final String TIMER_STOPPED_MESSAGE = "Countdown stopped\n";
}
