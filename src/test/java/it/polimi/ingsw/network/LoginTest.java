package it.polimi.ingsw.network;
import it.polimi.ingsw.AdrenalineLauncherClient;
import it.polimi.ingsw.AdrenalineLauncherServer;
import it.polimi.ingsw.generics.Bottleneck;
import it.polimi.ingsw.view.cli.CLIParser;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.rmi.NotBoundException;


class LoginTest {
    private final boolean SOCKET_TESTING = false;
    private final boolean REMOTE_TESTING = false;
    private final boolean GUI_TEST = false;
    static int j = 0;
    Bottleneck bottleneck = new Bottleneck();

    @Test
    void test2() throws InterruptedException {
        if(!GUI_TEST)
            return;
        AdrenalineLauncherClient.main(new String[]{});
        Thread.sleep(200000);
    }
    @Test
    void test32() throws InterruptedException {
        if(GUI_TEST)
            return;
        AdrenalineLauncherClient.main(new String[]{});
        Thread.sleep(200000);
    }

    @Test
    void mainStart() throws InterruptedException {
        if(GUI_TEST)
            return;

        (new Thread(() -> AdrenalineLauncherServer.main(new String[]{}))).start();
        Thread.sleep(300);

        //AdrenalineLauncherClient.main(new String[]{});

        for(int h = 0; h < 1; h++, j=0)
            for (int i = 0; i < 0; i++) {
                (new Thread(() -> Bot.login(Bot.getLoginString(++j), true, "127.0.0.1", 9999))).start();
                Thread.sleep(1000);
            }

        Thread.sleep(2000000);
    }
    /*
    @Test
    void callbackClient() throws Exception {
        if(!REMOTE_TESTING)
            return;

        AdrenalineClientRMI adrenalineClient = new AdrenalineClientRMI();
        RMIClient<IAdrenalineServer, AdrenalineClientRMI> client = RMIClient.connect("127.0.0.1",1099, adrenalineClient);
        adrenalineClient.initialize(client.server);
        while(!triggered)
            ;
    }
    */
}
