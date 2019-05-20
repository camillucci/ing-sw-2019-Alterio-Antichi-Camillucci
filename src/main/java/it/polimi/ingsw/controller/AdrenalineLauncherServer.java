package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.rmi.AdrenalineServerRMI;
import it.polimi.ingsw.network.rmi.ICallbackAdrenalineClient;
import it.polimi.ingsw.network.rmi.RMIListener;
import it.polimi.ingsw.network.rmi.RMIServer;
import it.polimi.ingsw.network.socket.AdrenalineServerSocket;
import it.polimi.ingsw.network.socket.TCPClient;
import it.polimi.ingsw.network.socket.TCPListener;
import it.polimi.ingsw.view.cli.CLIParser;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;

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
