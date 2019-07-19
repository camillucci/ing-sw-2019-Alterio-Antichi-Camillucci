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
        //String serverName = args.length > 1 ? args[1] : properties.getProperty("ipAddress", "127.0.0.1");
        String networkType = args.length > 1 ? args[1] : properties.getProperty("networkType", "socket");
        int socketPort = args.length > 2 ? Integer.parseInt(args[2]) : Integer.parseInt(properties.getProperty("socketPort", "9999"));
        int rmiPort = args.length > 3 ? Integer.parseInt(args[3]) : Integer.parseInt(properties.getProperty("rmiPort", "1099"));

        /*
        try {
            View view = viewType.equals("gui") ? new GUIView() : new CLIView();
            view.getLogin().socketEvent.addTmpEventHandler((a, isSocket) -> {
                try {
                    AdrenalineClient client = isSocket ? new AdrenalineClientSocket(serverName, socketPort, view) : new AdrenalineClientRMI(serverName, rmiPort, view);
                    client.start();
                } catch (IOException | NotBoundException  e) {
                    logger.log(Level.WARNING, e.getMessage());
                }
            });
            view.getLogin().askConnection();
        }
        */
        try {
            View view = viewType.equals("gui") ? new GUIView() : new CLIView();
            view.getLogin().ipAddressEvent.addTmpEventHandler((a, ipAddress) -> {
                try {
                    AdrenalineClient client = networkType.equals("socket") ? new AdrenalineClientSocket(ipAddress, socketPort, view) : new AdrenalineClientRMI(ipAddress, rmiPort, view);
                    client.start();
                } catch (IOException | NotBoundException  e) {
                    logger.log(Level.WARNING, e.getMessage());
                }
            });
            view.getLogin().askIpAddress();
        }
        catch(IOException | InterruptedException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    public void start() throws IOException, NotBoundException {
        client.start();
    }
}
