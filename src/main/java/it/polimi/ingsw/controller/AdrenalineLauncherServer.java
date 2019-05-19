package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.rmi.AdrenalineServerRMI;
import it.polimi.ingsw.network.rmi.ICallbackAdrenalineClient;
import it.polimi.ingsw.network.rmi.RMIListener;
import it.polimi.ingsw.network.rmi.RMIServer;
import it.polimi.ingsw.network.socket.AdrenalineServerSocket;
import it.polimi.ingsw.network.socket.TCPClient;
import it.polimi.ingsw.network.socket.TCPListener;

import java.io.IOException;
import java.rmi.RemoteException;

public class AdrenalineLauncherServer
{
    private TCPListener listenerTCP;
    private RMIListener<AdrenalineServerRMI, ICallbackAdrenalineClient> listenerRMI;

    public AdrenalineLauncherServer(boolean socket, int port) throws RemoteException {
        if(socket) {
            listenerTCP = new TCPListener(port);
            listenerTCP.newClientEvent.addEventHandler((a, client) -> onNewTCPClient(client));
        }
        else {
            listenerRMI = new RMIListener<>(port, AdrenalineServerRMI::new);
            listenerRMI.newClientEvent.addEventHandler((a, rmiServer) -> onNewRMIClient(rmiServer));
        }
    }

    public void start() throws IOException {
        if(listenerTCP != null)
            listenerTCP.start();
        else
            listenerRMI.start();
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
        Thread thread = new Thread((new AdrenalineServerSocket(client))::start);
        thread.start();
    }
}
