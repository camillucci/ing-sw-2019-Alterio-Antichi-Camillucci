import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.rmi.AdrenalineServerRMI;
import org.junit.jupiter.api.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class RMITMPTest
{
    boolean REMOTE_TESTING = true;
    @Test
    void server() throws RemoteException, InterruptedException {
        if(!REMOTE_TESTING)
            return;
        RMITMP.Server server = new RMITMP.Server(1099);
        Thread.sleep(80000);
    }


    @Test
    void client() throws RemoteException, InterruptedException, NotBoundException {
        if(!REMOTE_TESTING)
            return;
        RMITMP.Client client = new RMITMP.Client("169.254.71.160", 1099);
    }
    @Test
    void test() throws RemoteException, NotBoundException {
    }

    @Test
    void serverRMI() throws InterruptedException {
        AdrenalineServerRMI serverRMI = new AdrenalineServerRMI(new Controller());
        Thread.sleep(2000000);
    }

}