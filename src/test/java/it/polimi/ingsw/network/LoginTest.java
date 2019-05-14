package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.rmi.AdrenalineClientRMI;
import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.network.rmi.RMIListener;
import org.junit.jupiter.api.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class LoginTest {
    int n = 0;

    @Test
    void loginRMI() throws Exception {
        RMIListener listener = new RMIListener<>(1099, () -> new AdrenalineServer(new Controller()));
        listener.newClientEvent.addEventHandler((a, b) -> n++);
        listener.start();
        AdrenalineClient client = new AdrenalineClientRMI("127.0.0.1", 1099);
        client.loginFinto(0, 0);
    }
}
