package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.Room;
import it.polimi.ingsw.generics.Bottleneck;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.view.View;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;


public abstract class AdrenalineServer implements IAdrenalineServer
{
    private final Controller controller;
    private Room joinedRoom;
    protected String name;
    private List<String> availableColors;
    private List<String> otherPlayers = new ArrayList<>();
    protected Bottleneck bottleneck = new Bottleneck();
    protected RemoteActionsHandler remoteActionsHandler;
    private BiConsumer<Room, Integer> timerStartEventHandler = (a, timeout) -> bottleneck.tryDo( () -> sendMessage(timerStartMessage(timeout)));
    private BiConsumer<Room, Integer> timerTickEventHandler = (a, timeLeft) -> bottleneck.tryDo( () -> sendMessage(timerTickMessage(timeLeft)));
    private BiConsumer<Room, Integer> timerStopEventHandler = (a, timeLeft) -> bottleneck.tryDo( () -> sendMessage(TIMER_STOPPED_MESSAGE));
    private BiConsumer<Room, String> newPlayerEventHandler = (a, name) -> notifyPlayer(name);
    private BiConsumer<Room, String> playerDisconnectedEventHandler = (a, name) -> notifyPlayerDisconnected(name);
    private BiConsumer<Room, Room.ModelEventArgs> modelUpdatedEventHandler = (a, model) -> bottleneck.tryDo( () -> onModelUpdated(model));
    private boolean newTurn = true;
    protected static final int PING_PERIOD = 1; // 1 millisecond to test synchronization todo change in final version

    @Override
    public void newActionCommand(Command<RemoteActionsHandler> command) {
       bottleneck.tryDo( () -> command.invoke(this.remoteActionsHandler));
    }

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

    protected void onExceptionGenerated(Exception e){
        removeEvents();
        controller.notifyPlayerDisconnected(name);
        e.printStackTrace();
    }
    private synchronized void notifyPlayerDisconnected(String name){
        otherPlayers.remove(name);
        bottleneck.tryDo(() -> sendMessage(playerDisconnectedMessage(name)));
    }

    protected abstract void startPinging();
    protected abstract void stopPinging();

    @Override
    public void setName(String name) throws IOException {
        if(controller.newPlayer(name)) {
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


    private void notifyIsHost() throws IOException {
        boolean isHost = joinedRoom.isHost(name);
        sendCommand(new Command<>(view -> view.getLogin().notifyHost(isHost)));
        if(!isHost)
            ready();
    }

    @Override
    public void setGameLength(int gameLength) {
        joinedRoom.setGameLength(gameLength);
    }
    @Override
    public void setGameMap(int choice) {
        joinedRoom.setGameSize(choice);
        ready();
    }

    private synchronized void notifyPlayer(String name){
        if(otherPlayers.contains(name))
            return;
        otherPlayers.add(name);
        bottleneck.tryDo(() -> sendMessage(newPlayerMessage(name)));
    }

    @Override
    public void ready() {
        joinedRoom.notifyPlayerReady(name);
        setupRoomEvents();
        for(String name : joinedRoom.getPlayerNames())
            notifyPlayer(name);
        stopPinging();
    }

    protected abstract void sendCommand(Command<View> command) throws IOException;

    protected void sendMessage(String message) throws IOException
    {
        sendCommand(new Command<>(v -> v.getCurViewElement().onNewMessage(message)));
    }

    private void removeEvents() {
        if(joinedRoom != null) {
            joinedRoom.timerStartEvent.removeEventHandler(timerStartEventHandler);
            joinedRoom.timerTickEvent.removeEventHandler(timerTickEventHandler);
            joinedRoom.timerStopEvent.removeEventHandler(timerStopEventHandler);
            joinedRoom.newPlayerEvent.removeEventHandler(newPlayerEventHandler);
            joinedRoom.playerDisconnectedEvent.removeEventHandler(playerDisconnectedEventHandler);
            if(joinedRoom.isMatchStarted() )
                joinedRoom.modelUpdatedEvent.removeEventHandler(modelUpdatedEventHandler);
        }
    }

    private void setupRoomEvents()
    {
        joinedRoom.timerStartEvent.addEventHandler(timerStartEventHandler);
        joinedRoom.timerTickEvent.addEventHandler(timerTickEventHandler);
        joinedRoom.timerStopEvent.addEventHandler(timerStopEventHandler);
        joinedRoom.newPlayerEvent.addEventHandler(newPlayerEventHandler);
        joinedRoom.playerDisconnectedEvent.addEventHandler(playerDisconnectedEventHandler);
        joinedRoom.modelUpdatedEvent.addEventHandler(modelUpdatedEventHandler);
    }
    private static final String MATCH_STARTING_MESSAGE = "Match is starting";
    private static String newPlayerMessage(String name){ return name + " joined the room";}
    private static String playerDisconnectedMessage(String name){ return name + " left the room";}
    private static String timerStartMessage(int timeout){
        return "Countdown is started: " + timeout + " seconds left\n";
    }
    private static String timerTickMessage(int timeLeft){
        return timeLeft + " seconds left\n";
    }
    private static final String TIMER_STOPPED_MESSAGE = "Countdown stopped\n";
}
