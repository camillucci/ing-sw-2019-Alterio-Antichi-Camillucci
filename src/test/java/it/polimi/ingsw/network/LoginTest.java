package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.rmi.*;
import org.junit.jupiter.api.Test;

public class LoginTest {
    boolean triggered;
    @Test
    void loginRMI() throws Exception {

        RMIListener listener = new RMIListener<>(1099, () -> new AdrenalineServerRMI(new Controller()));
        listener.start();
        AdrenalineClient client = new AdrenalineClientRMI("127.0.0.1", 1099);
        client.matchSnapshotUpdatedEvent.addEventHandler((a,b) -> this.triggered = true);
        client.connect();
        client.loginFinto(2, 2);
        while(!triggered)
            ;
        listener.stop();
    }
}
