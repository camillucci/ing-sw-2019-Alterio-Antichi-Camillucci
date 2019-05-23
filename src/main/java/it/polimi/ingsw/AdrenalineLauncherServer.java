package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.rmi.*;
import it.polimi.ingsw.network.socket.*;
import it.polimi.ingsw.view.cli.CLIParser;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdrenalineLauncherServer
{
    private TCPListener listenerTCP;
    private RMIListener<AdrenalineServerRMI, ICallbackAdrenalineClient> listenerRMI;
    private CLIParser parser = new CLIParser(System.in);
    private Controller controller = new Controller();

    public AdrenalineLauncherServer(boolean socket, int port) throws RemoteException {
        if(socket) {
            listenerTCP = new TCPListener(port);
            listenerTCP.newClientEvent.addEventHandler((a, client) -> onNewTCPClient(client));
        }
        else {
            listenerRMI = new RMIListener<>(port, () -> new AdrenalineServerRMI(controller));
            listenerRMI.newClientEvent.addEventHandler((a, rmiServer) -> onNewRMIClient(rmiServer));
        }
    }

    public static void main(String[] args) {
        Logger logger = Logger.getLogger("client");
        InputStream input = AdrenalineLauncherClient.class.getClassLoader().getResourceAsStream("serverConfig.properties");
        Properties properties = new Properties();

        TCPListener listenerTCP = null;
        RMIListener<AdrenalineServerRMI, ICallbackAdrenalineClient> listenerRMI = null;
        CLIParser parser = new CLIParser(System.in);
        Controller controller = new Controller();

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
            port = Integer.parseInt(args[0]);
        else
            port = Integer.parseInt(properties.getProperty("port", "21508"));


        if(args.length > 1)
            networkType = args[1];
        else
            networkType = properties.getProperty("network", "socket");

        try {
            if (networkType.equals("rmi")) {
                listenerRMI = new RMIListener<>(port, () -> new AdrenalineServerRMI(controller));
                listenerRMI.newClientEvent.addEventHandler((a, rmiServer) -> rmiServer.server().initialize(rmiServer.client()));
                listenerRMI.start();
            } else {
                listenerTCP = new TCPListener(port);
                listenerTCP.newClientEvent.addEventHandler((a, client) -> {
                    Thread thread = new Thread((new AdrenalineServerSocket(client, controller))::start);
                    thread.start();
                });
                listenerTCP.start();
            }
            do{
                System.out.println("Press 1 to close");
            }
            while(parser.parseChoice() != 1);
            if(listenerTCP != null)
                listenerTCP.stop();
            else
                listenerRMI.stop();
        }
        catch (IOException e) {
            logger.log(Level.WARNING, "Could not start server");
        }
    }

    public void setInputStream(InputStream stream){
        parser = new CLIParser(stream);
    }

    public void start() throws IOException {
        if(listenerTCP != null)
            listenerTCP.start();
        else
            listenerRMI.start();
        do{
            System.out.println("Press 1 to close");
        }
        while(parser.parseChoice() != 1);
        this.stop();
    }

    public void stop(){
        if(listenerTCP != null)
            listenerTCP.stop();
        else
            listenerRMI.stop();
    }

    private void onNewRMIClient(RMIServer<AdrenalineServerRMI, ICallbackAdrenalineClient> server){
        server.server().initialize(server.client());
    }

    private void onNewTCPClient(TCPClient client){
        Thread thread = new Thread((new AdrenalineServerSocket(client, controller))::start);
        thread.start();
    }
}