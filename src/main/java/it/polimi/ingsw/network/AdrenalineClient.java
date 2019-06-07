package it.polimi.ingsw.network;
import it.polimi.ingsw.generics.Bottleneck;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.view.View;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.List;

public abstract class AdrenalineClient
{
    protected View view;
    protected String serverName;
    protected int serverPort;
    protected Bottleneck bottleneck = new Bottleneck();
    protected boolean matchStarted;
    protected static final int PING_PERIOD = 1; // 1 millisecond to test ping synchronization  todo change in final version

    public AdrenalineClient(String serverName, int serverPort, View view)
    {
        this.view = view;
        this.serverName = serverName;
        this.serverPort = serverPort;
        bottleneck.exceptionGenerated.addEventHandler((a, exception) -> onExceptionGenerated(exception));
        setupView();
    }

    protected synchronized void onExceptionGenerated(Exception exception){
        view.getCurViewElement().onNewMessage("I'm sorry, you have been disconnected");
    }

    protected void setupView(){
        view.getLogin().nameEvent.addEventHandler((a, name) -> bottleneck.tryDo( () -> notifyName(name)));
        view.getLogin().colorEvent.addEventHandler((a, color) -> bottleneck.tryDo(() -> notifyColor(color)));
        view.getLogin().gameLengthEvent.addEventHandler((a, len) -> bottleneck.tryDo(() -> notifyGameLength(len)));
        view.getLogin().gameMapEvent.addEventHandler((a, map) -> bottleneck.tryDo(() -> notifyGameMap(map)));
        view.getActionHandler().choiceEvent.addEventHandler((a, action) -> bottleneck.tryDo( () -> initializeAction(action)));
    }

    public void start() throws IOException, NotBoundException {
        connect();
        startPing();
        view.getLogin().login();
    }

    protected abstract void startPing();
    protected abstract void connect() throws IOException, NotBoundException;
    protected abstract void notifyName(String name) throws IOException, ClassNotFoundException;
    protected abstract void notifyColor(int colorIndex) throws IOException, ClassNotFoundException;
    protected abstract void notifyGameLength(int gameLength) throws IOException, ClassNotFoundException;
    protected abstract void notifyGameMap(int gameMap) throws IOException, ClassNotFoundException;
    protected abstract void stopPing();
    public void modelChanged(MatchSnapshot matchSnapshot)  {
        view.getCurViewElement().onModelChanged(matchSnapshot);
    }
    public abstract void newActions(List<RemoteAction> newActions) throws IOException, ClassNotFoundException;
    protected abstract void initializeAction(RemoteAction action) throws IOException;
    public void newMessage(String message) {
        view.getCurViewElement().onNewMessage(message);
        if(message.contains("Match is starting")) {
            view.loginCompleted();
            stopPing();
            matchStarted = true;
        }
    }
}
