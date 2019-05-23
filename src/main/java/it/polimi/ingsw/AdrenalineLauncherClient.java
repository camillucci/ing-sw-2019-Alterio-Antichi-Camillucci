package it.polimi.ingsw;

import it.polimi.ingsw.network.AdrenalineClient;
import it.polimi.ingsw.network.rmi.AdrenalineClientRMI;
import it.polimi.ingsw.network.socket.AdrenalineClientSocket;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.CLIView;
import it.polimi.ingsw.view.gui.GUIView;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdrenalineLauncherClient
{
    private AdrenalineClient client;

    public AdrenalineLauncherClient(boolean gui, boolean socket, String serverName, int port) throws IOException {
        View view = gui ? new GUIView() : new CLIView();
        client = socket ? new AdrenalineClientSocket(serverName, port, view) : new AdrenalineClientRMI(serverName, port, view);
    }

    public static void main(String[] args) {
        Logger logger = Logger.getLogger("client");
        InputStream input = AdrenalineLauncherClient.class.getClassLoader().getResourceAsStream("clientConfig.properties");
        Properties properties = new Properties();

        View view;
        AdrenalineClient client;

        String viewType;
        String serverName;
        int port;
        String networkType;

        if(input != null) {
            try {
                properties.load(input);
            } catch (IOException e) {
                logger.log(Level.WARNING, "No config file found");
            }
        }

        if(args.length > 0)
            viewType = args[0];
        else
            viewType = properties.getProperty("view");

        if(viewType.equals("gui"))
            view = new GUIView();
        else
            view = new CLIView();

        if(args.length > 1)
            serverName = args[1];
        else
            serverName = properties.getProperty("ipAddress", "127.0.0.1");

        if(args.length > 2)
            port = Integer.parseInt(args[2]);
        else
            port = Integer.parseInt(properties.getProperty("port", "21508"));

        if(args.length > 3)
            networkType = args[3];
        else
            networkType = properties.getProperty("network", "socket");

        try {
            if(networkType.equals("rmi"))
                client = new AdrenalineClientRMI(serverName, port, view);
            else
                client = new AdrenalineClientSocket(serverName, port, view);
            client.start();
            }
            catch (IOException | NotBoundException e) {
                logger.log(Level.WARNING, "Could not start client");
        }
    }

    public void start() throws IOException, NotBoundException {
        client.start();
    }
}
