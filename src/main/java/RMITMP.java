import it.polimi.ingsw.network.rmi.RMIServer;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMITMP
{
    public interface RemoteInterface extends Remote{
        void newMessage(String message) throws RemoteException;
        void register(RemoteInterface remoteInterface) throws RemoteException;
    }

    public static class Server implements RemoteInterface
    {
        Registry registry;

        public Server (int port) throws RemoteException {
            registry = LocateRegistry.createRegistry(port);
            Remote stub = UnicastRemoteObject.exportObject(this, port);
            LocateRegistry.getRegistry(port).rebind("Server", stub);
        }

        @Override
        public void newMessage(String message) {
            System.out.println(message);
        }

        @Override
        public void register(RemoteInterface remoteInterface) throws RemoteException {
            System.out.println("Registered");
            remoteInterface.newMessage("TEST");
        }
    }

    public static class Client implements RemoteInterface {

        public Client(String serverName, int port) throws RemoteException, NotBoundException {
            UnicastRemoteObject.exportObject(this, 1099);
            RemoteInterface remoteServer = (RemoteInterface) LocateRegistry.getRegistry(serverName, port).lookup("Server");
            remoteServer.register(this);
        }

        @Override
        public void newMessage(String message) {
            System.out.println(message);
        }

        @Override
        public void register(RemoteInterface remoteInterface) throws RemoteException {
            // nothing
        }
    }

}
