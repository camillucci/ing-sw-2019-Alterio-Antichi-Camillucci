package it.polimi.ingsw.network;
import it.polimi.ingsw.generics.Bottleneck;
import it.polimi.ingsw.network.rmi.ICallbackAdrenalineClient;
import it.polimi.ingsw.view.View;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Class used by client for communication with server. It's abstract because it needs to be implemented once user has
 * decided whether they want Socket based or RMI based communication. This class contains all methods used to
 * communicate user's choices to server.
 */
public abstract class AdrenalineClient
{
    /**
     * Reference to the interface which communicates with the user
     */
    protected View view;
    protected String serverName;
    /**
     * Port of the server this class communicates with
     */
    protected int serverPort;
    protected Bottleneck bottleneck = new Bottleneck();
    /**
     * Simple boolean class used to keep track of match state
     */
    protected boolean matchStarted;
    protected static final int PING_PERIOD = 1; // 1 millisecond to test ping synchronization  todo change in final version

    /**
     * Sets all parameters to input values. Also signs up to bottleneck event, which triggers when connection is lost.
     * @param view Reference to the interface which communicates with the user
     */
    public AdrenalineClient(View view)
    {
        this.view = view;
        bottleneck.exceptionGenerated.addEventHandler((a, exception) -> onExceptionGenerated(exception));
        setupView();
    }

    protected synchronized void onExceptionGenerated(Exception exception){
        exception.printStackTrace();
        view.getCurViewElement().onNewMessage("I'm sorry, you have been disconnected");
    }

    /**
     * Signs up to name, color game length, map type and new command events. Once an event triggers, this class
     * communicates user's choice to server
     */
    protected void setupView(){
        view.getLogin().nameEvent.addEventHandler((a, name) -> bottleneck.tryDo( () -> notifyName(name)));
        view.getLogin().colorEvent.addEventHandler((a, color) -> bottleneck.tryDo(() -> notifyColor(color)));
        view.getLogin().gameLengthEvent.addEventHandler((a, len) -> bottleneck.tryDo(() -> notifyGameLength(len)));
        view.getLogin().gameMapEvent.addEventHandler((a, map) -> bottleneck.tryDo(() -> notifyGameMap(map)));
        view.getActionHandler().newCommand.addEventHandler((a, command) -> bottleneck.tryDo( () -> notifyActionCommand(command)));
    }

    /**
     * Connects to server using the global parameters. Also starts login method, which displays the intro to the user
     * @throws IOException
     * @throws NotBoundException
     */
    public void start() throws IOException, NotBoundException {
        connect();
        startPing();
        view.getLogin().login();
    }

    protected abstract void startPing();

    /**
     * Starts a communication with the server using the global parameters, namely port and server string.
     * @throws IOException
     * @throws NotBoundException
     */
    protected abstract void connect() throws IOException, NotBoundException;

    /**
     * Communicates to server user's name of choice, once the event has been triggered
     * @param name
     * @throws IOException
     * @throws ClassNotFoundException
     */
    protected void notifyName(String name) throws IOException, ClassNotFoundException {
        sendServerCommand(new Command<>(server -> server.setName(name)));
    }

    /**
     * Communicates to server user's color of choice, once the event has been triggered. Color is communicated through
     * a number.
     * @param colorIndex number that represents color chosen by the user.
     * @throws IOException
     */
    private void notifyColor(int colorIndex) throws IOException {
        sendServerCommand(new Command<>(server -> server.setColor(colorIndex)));
    }

    /**
     * Communicates to server user's game length of choice, once the event has been triggered.
     * @param gameLength Number that represents how many skulls the user wants to have in the game.
     * @throws IOException
     */
    private void notifyGameLength(int gameLength) throws IOException
    {
        sendServerCommand(new Command<>(server -> server.setGameLength(gameLength)));
    }

    /**
     * Communicates to server user's map type of choice, once the event has been triggered.
     * @param gameMap Number that represents which map the user has chosen among the 4 possible ones.
     * @throws IOException
     */
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
