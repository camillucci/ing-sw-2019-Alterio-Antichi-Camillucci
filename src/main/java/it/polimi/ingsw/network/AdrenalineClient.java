package it.polimi.ingsw.network;
import it.polimi.ingsw.generics.Bottleneck;
import it.polimi.ingsw.view.View;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class used by client for communication with server. It's abstract because it needs to be implemented once user has
 * decided whether they want Socket based or RMI based communication. This class contains all methods used to
 * communicate user's choices to server.
 */
public abstract class AdrenalineClient
{
    /**
     * Reference to the class that communicates with the user
     */
    protected View view;
    /**
     * Reference to an "Exception's bottleneck". All methods that can throw a fatal exception have to pass through the tryDO function of this class.
     * If an exception is thrown then an event is invoked: onExceptionGenerated is the event-handler and the Exception is the event-arg
     */
    protected Bottleneck bottleneck = new Bottleneck();
    private static final Logger logger = Logger.getLogger("AdrenalineClient");
    /**
     * Time interval in milliseconds that elapse between two attempts of pinging the server
     */
    protected static final int PING_PERIOD = 1000;

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

    /**
     * Handle all the exceptions of the class. if this method is invoked, then the server is unreachable.
     * @param exception the specific current exception.
     */

    protected synchronized void onExceptionGenerated(Exception exception){
        logger.log(Level.WARNING, exception.getMessage());
        view.getCurViewElement().onNewMessage("Sorry, i'm unable to connect");
    }

    /**
     * Signs up to name, color, game length, map type and new command events. Once an event triggers, this class
     * communicates user's choice to server
     */
    protected void setupView(){
        view.getLogin().nameEvent.addEventHandler((a, name) -> notifyName(name));
        view.getLogin().colorEvent.addEventHandler((a, color) -> notifyColor(color));
        view.getLogin().gameLengthEvent.addEventHandler((a, len) -> notifyGameLength(len));
        view.getLogin().gameMapEvent.addEventHandler((a, map) ->notifyGameMap(map));
        view.getActionHandler().newCommand.addEventHandler((a, command) -> notifyActionCommand(command));
    }

    /**
     * Connects to server using the global parameters. Also starts login method, which displays the intro to the user
     * @throws IOException IOException
     * @throws NotBoundException NotBoundException
     */
    public void start() throws IOException, NotBoundException {
        connect();
        startPing();
        view.getLogin().login();
    }

    /**
     * Starts the pinging routine
     */
    protected abstract void startPing();

    /**
     * Starts a communication with the server using the global parameters, namely port and server string.
     * @throws IOException IOException
     * @throws NotBoundException NotBoundException
     */
    protected abstract void connect() throws IOException, NotBoundException;

    /**
     * Communicates to server user's name of choice, once the event has been triggered
     * @param name Name chosen by the user
     */
    protected void notifyName(String name)  {
        this.notifyServerCommand(new Command<>(server -> server.setName(name)));
    }

    /**
     * Communicates to server user's color of choice, once the event has been triggered. Color is communicated through
     * a number.
     * @param colorIndex number that represents color chosen by the user.
     */
    private void notifyColor(int colorIndex){
        this.notifyServerCommand(new Command<>(server -> server.setColor(colorIndex)));
    }

    /**
     * Communicates to server user's game length of choice, once the event has been triggered.
     * @param gameLength Number that represents how many skulls the user wants to have in the game.
     */
    private void notifyGameLength(int gameLength)
    {
        this.notifyServerCommand(new Command<>(server -> server.setGameLength(gameLength)));
    }

    /**
     * Communicates to server user's map type of choice, once the event has been triggered.
     * @param gameMap Number that represents which map the user has chosen among the 4 possible ones.
     */
    private void notifyGameMap(int gameMap)
    {
        this.notifyServerCommand(new Command<>(server -> server.setGameMap(gameMap)));
    }

    /**
     * Notifies the server about a new command. This kind of command is used in the action phase of the game
     * and it's used to modify the model.
     * @param command Command to be sent.
     */
    private void notifyActionCommand (Command<RemoteActionsHandler> command)
    {
        this.notifyServerCommand(new Command<>(server -> server.newActionCommand(command)));
    }
    /**
     * Sends to server a new command. This is  the only way client communicates with server
     * @param command Command to be sent.
     */
    protected void notifyServerCommand(Command<IAdrenalineServer> command)
    {
        bottleneck.tryDo(() -> sendServerCommand(command));
    }
    protected abstract void sendServerCommand(Command<IAdrenalineServer> command) throws IOException;

    /**
     * Invoked when a new View-command is received by the Server.
     * That kind of command is used by server to invoke methods of the Client's view
     * @param command Command received.
     */
    public void newViewCommand(Command<View> command){
        bottleneck.tryDo(() -> command.invoke(view));
    }

    /**
     * Stops the pinging routine
     */
    protected abstract void stopPing();
}
