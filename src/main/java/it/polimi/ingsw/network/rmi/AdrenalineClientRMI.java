package it.polimi.ingsw.network.rmi;
import it.polimi.ingsw.network.AdrenalineClient;
import it.polimi.ingsw.network.IAdrenalineServer;
import it.polimi.ingsw.network.IRemoteActionHandler;
import it.polimi.ingsw.view.View;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.List;

public class AdrenalineClientRMI extends AdrenalineClient {
    private IAdrenalineServer server;
    private IRemoteActionHandler remoteActionHandler;

    public AdrenalineClientRMI(String serverName, int serverPort, View view) {
        super(serverName, serverPort, view);
    }

    @Override
    protected void setupView()
    {
        view.getLogin().nameEvent.addEventHandler((a, name) -> bottleneck.tryDo( () -> notifyName(name)));
        view.getLogin().colorEvent.addEventHandler((a, color) -> bottleneck.tryDo(() -> notifyColor(color)));
        view.getLogin().gameLengthEvent.addEventHandler((a, len) -> bottleneck.tryDo(() -> server.setGameLength(len)));
        view.getLogin().gameMapEvent.addEventHandler((a, map) -> bottleneck.tryDo(() -> server.setGameMap(map)));
    }

    @Override
    protected void connect() throws IOException, NotBoundException {
        server = RMIClient.<IAdrenalineServer, ICallbackAdrenalineClient>connect(serverName, serverPort, this).server;
    }

    public void initialize(IAdrenalineServer server)
    {
        this.server = server;
    }

    @Override
    protected void notifyColor(int colorIndex) throws IOException {
        if(!server.setColor(colorIndex))
            view.getLogin().notifyAvailableColor(server.availableColors());
        else {
            boolean isHost = server.isHost();
            if (isHost) {
                view.getLogin().gameMapEvent.addEventHandler((a, map) -> bottleneck.tryDo(() -> server.ready()));
                view.getLogin().notifyHost(true);
            } else {
                view.getLogin().notifyHost(false);
                server.ready();
            }
        }
    }

    @Override
    protected void notifyName(String name) throws IOException {
        boolean accepted = server.setName(name);
        view.getLogin().notifyAccepted(accepted);
        if(accepted)
            view.getLogin().notifyAvailableColor(server.availableColors());
    }

    protected void manageActions(List<RemoteActionRMI> options) throws IOException, ClassNotFoundException {
        view.getActionHandler().choiceEvent.addEventHandler((a, choice) -> bottleneck.tryDo( () -> options.get(choice).initialize(remoteActionHandler))); //communicates choice to server
        view.getActionHandler().chooseAction(new ArrayList<>(options));
    }
}
