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

    public AdrenalineLauncherClient(boolean gui, boolean socket, String serverName, int port) throws IOException, InterruptedException {
        View view = gui ? new GUIView() : new CLIView();
        client = socket ? new AdrenalineClientSocket(serverName, port, view) : new AdrenalineClientRMI(serverName, port, view);
    }

    public static void main(String[] args) {
        Logger logger = Logger.getLogger("AdrenalineLauncherClient");
        InputStream input = AdrenalineLauncherClient.class.getClassLoader().getResourceAsStream("clientConfig.properties");
        Properties properties = new Properties();

        if (input != null)
            try {
                properties.load(input);
            } catch (IOException e) {
                logger.log(Level.WARNING, "No config file found");
            }

        String viewType = args.length > 0 ? args[0] : properties.getProperty("view", "cli");
        String networkType = args.length > 1 ? args[1] : properties.getProperty("network", "socket");
        String serverName = args.length > 2 ? args[2] : properties.getProperty("ipAddress", "127.0.0.1");
        int socketPort = args.length > 3 ? Integer.parseInt(args[3]) : Integer.parseInt(properties.getProperty("socketPort", "9999"));
        int rmiPort = args.length > 4 ? Integer.parseInt(args[4]) : Integer.parseInt(properties.getProperty("rmiPort", "1099"));

        try {
            View view = viewType.equals("gui") ? new GUIView() : new CLIView();
            AdrenalineClient client = networkType.equals("rmi") ? new AdrenalineClientRMI(serverName, rmiPort, view) : new AdrenalineClientSocket(serverName, socketPort, view);
            client.start();
        }
        catch(IOException | NotBoundException | InterruptedException e) {
            logger.log(Level.WARNING, "Could not start client");
        }
    }

    public void start() throws IOException, NotBoundException {
        client.start();
    }
}
