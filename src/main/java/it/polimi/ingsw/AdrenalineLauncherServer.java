package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.rmi.*;
import it.polimi.ingsw.network.socket.*;
import it.polimi.ingsw.view.cli.CLIMessenger;
import it.polimi.ingsw.view.cli.CLIParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdrenalineLauncherServer
{
    private static final int PINGING_PERIOD = 1000; //todo change
    private static CLIParser parser;
    public static void main(String[] args) {
        Logger logger = Logger.getLogger("AdrenalineLauncherServer");
        InputStream input = AdrenalineLauncherClient.class.getClassLoader().getResourceAsStream("serverConfig.properties");
        Properties properties = new Properties();

        TCPListener listenerTCP;
        RMIListener listenerRMI;
        if(parser == null)
            parser = new CLIParser(System.in);

        if(input != null)
            try {
                properties.load(input);
            } catch (IOException e) {
                logger.log(Level.WARNING, "No config file found");
            }

        int socketPort = args.length > 0 ? Integer.parseInt(args[0]) : Integer.parseInt(properties.getProperty("socketPort", "9999"));
        int rmiPort = args.length > 1 ? Integer.parseInt(args[1]) : Integer.parseInt(properties.getProperty("rmiPort", "1099"));
        int loginTimer  = args.length > 2 ? Integer.parseInt(args[2]) : Integer.parseInt(properties.getProperty("loginTimer", "30"));
        int turnTimer = args.length > 3 ? Integer.parseInt(args[3]) : Integer.parseInt(properties.getProperty("turnTimer", "120"));

        Controller controller = new Controller(loginTimer, turnTimer);

        try {
            listenerRMI = new RMIListener(rmiPort, () -> new AdrenalineServerRMI(controller));
            listenerRMI.pingAll(PINGING_PERIOD);
            listenerRMI.start();
            listenerTCP = new TCPListener(socketPort);
            listenerTCP.newClientEvent.addEventHandler((a, client) -> {
                Thread thread = new Thread((new AdrenalineServerSocket(client, controller))::start);
                thread.start();
            });
            listenerTCP.pingAll(PINGING_PERIOD);
            listenerTCP.start();
            Scanner scanner = new Scanner(System.in);
            do {
                logger.log(Level.INFO, "Press 1 to close");
            }
            while(getChoice() != 1);
            listenerTCP.stop();
            for(TCPClient client : listenerTCP.getConnected())
                client.close();
            listenerRMI.stop();
            listenerRMI.closeAll();
            logger.log(Level.INFO, "Server closed");
        }
        catch (IOException e) {
            logger.log(Level.WARNING, "Could not start server");
        }
        finally{
            System.exit(0);
        }

    }

    private static int getChoice() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
           return Integer.parseInt(reader.readLine());
        }
        catch(NumberFormatException | IOException e) {return -1;}
    }

    public static void setParser(CLIParser parser)
    {
        AdrenalineLauncherServer.parser = parser;
    }
}
