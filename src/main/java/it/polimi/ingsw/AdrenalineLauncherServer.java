package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.rmi.*;
import it.polimi.ingsw.network.socket.*;
import it.polimi.ingsw.view.cli.CLIParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdrenalineLauncherServer
{
    private static CLIParser parser;
    public static void main(String[] args) {
        Logger logger = Logger.getLogger("AdrenalineLauncherServer");
        InputStream input = AdrenalineLauncherClient.class.getClassLoader().getResourceAsStream("serverConfig.properties");
        Properties properties = new Properties();

        TCPListener listenerTCP;
        RMIListener listenerRMI;
        if(parser == null)
            parser = new CLIParser(System.in);
        Controller controller = new Controller();

        if(input != null)
            try {
                properties.load(input);
            } catch (IOException e) {
                logger.log(Level.WARNING, "No config file found");
            }

        int socketPort = args.length > 0 ? Integer.parseInt(args[0]) : Integer.parseInt(properties.getProperty("socketPort", "9999"));
        int rmiPort = args.length > 1 ? Integer.parseInt(args[1]) : Integer.parseInt(properties.getProperty("rmiPort", "1099"));
        properties.setProperty("socketPort", Integer.toString(socketPort));
        properties.setProperty("rmiPort", Integer.toString(rmiPort));

        try {
            listenerRMI = new RMIListener(rmiPort, () -> new AdrenalineServerRMI(controller));
            listenerRMI.start();
            listenerTCP = new TCPListener(socketPort);
            listenerTCP.newClientEvent.addEventHandler((a, client) -> {
                Thread thread = new Thread((new AdrenalineServerSocket(client, controller))::start);
                thread.start();
            });
            listenerTCP.start();

            do {
                logger.log(Level.INFO, "Press 1 to close");
            }
            while(parser.parseChoice() != 1);
            listenerTCP.stop();
            for(TCPClient client : listenerTCP.getConnected())
                client.close();
            listenerRMI.stop();
            logger.log(Level.INFO, "Server closed");

        }
        catch (IOException e) {
            logger.log(Level.WARNING, "Could not start server");
        }
        finally{
            System.exit(0);
        }

    }

    public static void setParser(CLIParser parser)
    {
        AdrenalineLauncherServer.parser = parser;
    }
}
