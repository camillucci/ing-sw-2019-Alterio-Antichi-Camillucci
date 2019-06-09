package it.polimi.ingsw.network;
import it.polimi.ingsw.generics.Bottleneck;
import it.polimi.ingsw.network.rmi.ICallbackAdrenalineClient;
import it.polimi.ingsw.view.View;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


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
        exception.printStackTrace();
        view.getCurViewElement().onNewMessage("I'm sorry, you have been disconnected");
    }

    protected void setupView(){
        view.getLogin().nameEvent.addEventHandler((a, name) -> bottleneck.tryDo( () -> notifyName(name)));
        view.getLogin().colorEvent.addEventHandler((a, color) -> bottleneck.tryDo(() -> notifyColor(color)));
        view.getLogin().gameLengthEvent.addEventHandler((a, len) -> bottleneck.tryDo(() -> notifyGameLength(len)));
        view.getLogin().gameMapEvent.addEventHandler((a, map) -> bottleneck.tryDo(() -> notifyGameMap(map)));
        view.getActionHandler().newCommand.addEventHandler((a, command) -> bottleneck.tryDo( () -> notifyActionCommand(command)));
    }

    public void start() throws IOException, NotBoundException {
        connect();
        startPing();
        view.getLogin().login();
    }

    protected abstract void startPing();
    protected abstract void connect() throws IOException, NotBoundException;
    protected void notifyName(String name) throws IOException, ClassNotFoundException {
        sendServerCommand(new Command<>(server -> server.setName(name)));
    }
    private void notifyColor(int colorIndex) throws IOException {
        sendServerCommand(new Command<>(server -> server.setColor(colorIndex)));
    }
    private void notifyGameLength(int gameLength) throws IOException
    {
        sendServerCommand(new Command<>(server -> server.setGameLength(gameLength)));
    }
    private void notifyGameMap(int gameMap) throws IOException
    {
        sendServerCommand(new Command<>(server -> server.setGameMap(gameMap)));
    }
    private void notifyActionCommand (Command<RemoteActionsHandler> command) throws IOException
    {
        sendServerCommand(new Command<>(server -> server.newActionCommand(command)));
    }
    public void newViewCommand(Command<View> command){
        bottleneck.tryDo(() -> command.invoke(view));
    }
    protected abstract void sendServerCommand(Command<IAdrenalineServer> command) throws IOException;
    protected abstract void stopPing();
}
