import org.junit.jupiter.api.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class RMITMPTest
{
    boolean REMOTE_TESTING = false;
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
        RMITMP.Client client = new RMITMP.Client("127.0.0.1", 1099);
    }
    @Test
    void test() throws RemoteException, NotBoundException {
    }

}