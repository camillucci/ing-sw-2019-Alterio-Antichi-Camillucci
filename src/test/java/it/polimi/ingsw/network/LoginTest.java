package it.polimi.ingsw.network;

import it.polimi.ingsw.AdrenalineLauncherClient;
import it.polimi.ingsw.AdrenalineLauncherServer;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.rmi.AdrenalineServerRMI;
import it.polimi.ingsw.network.rmi.ICallbackAdrenalineClient;
import it.polimi.ingsw.network.rmi.RMIListener;
import it.polimi.ingsw.view.cli.CLIParser;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static it.polimi.ingsw.generics.Utils.tryDo;

public class LoginTest {
    private final boolean REMOTE_TESTING = false;
    private final boolean GUI_TEST = true;
    static int j = 0;
    @Test
    void loginSocket_CLI() throws IOException, NotBoundException, InterruptedException {
        String userInput = "turangla_lella\n1\n5\n10\n";
        String serverInput = "1\n";
        PipedOutputStream serverStreamWrite = new PipedOutputStream();
        InputStream serverStreamRead = new PipedInputStream(serverStreamWrite);
        CLIParser.parser.setInputStream(new ByteArrayInputStream(userInput.getBytes()));// set Input stream for CLIParser to userInput String
        AdrenalineLauncherServer server = new AdrenalineLauncherServer(true, 10000);
        server.setInputStream(serverStreamRead);
        (new Thread(() -> tryDo(() -> server.start()))).start();
        Thread.sleep(100);
        AdrenalineLauncherClient client = new AdrenalineLauncherClient(false, true, "127.0.0.1", 10000);
        (new Thread( () -> {
            try {
                client.start();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        })).start();
        Thread.sleep(2000);
    }

    @Test
    void LoginRMI_CLI() throws IOException, NotBoundException, InterruptedException {
        String userInput = "turangla_lella\n1\n5\n10\n";
        String serverInput = "1\n";
        PipedOutputStream serverStreamWrite = new PipedOutputStream();
        InputStream serverStreamRead = new PipedInputStream(serverStreamWrite);
        CLIParser.parser.setInputStream(new ByteArrayInputStream(userInput.getBytes())); // set Input stream for CLIParser to userInput String
        AdrenalineLauncherServer server = new AdrenalineLauncherServer(false, 10001);
        server.setInputStream(serverStreamRead);
        (new Thread(() -> tryDo(() -> server.start()))).start();
        Thread.sleep(100);
        AdrenalineLauncherClient client = new AdrenalineLauncherClient(false, false, "127.0.0.1", 10001);
        client.start();
        serverStreamWrite.write(serverInput.getBytes());
    }

    @Test
    void multiple_loginRMI_CLI() throws IOException, InterruptedException {
        String serverInput = "1\n";
        PipedOutputStream serverStreamWrite = new PipedOutputStream();
        InputStream serverStreamRead = new PipedInputStream(serverStreamWrite);
        AdrenalineLauncherServer server = new AdrenalineLauncherServer(false, 10004);
        server.setInputStream(serverStreamRead);
        (new Thread(() -> tryDo(() -> server.start()))).start();
        Thread.sleep(300);
        for(int i=0; i < 4; i++) {
            Bot.login(Bot.getLoginString(i+1), false, "127.0.0.1", 10004);
            //Thread.sleep(3000);
        }
        Thread.sleep(9000);
    }

    @Test
    void multiple_loginSocket_CLI() throws IOException, InterruptedException {
        String serverInput = "1\n";
        PipedOutputStream serverStreamWrite = new PipedOutputStream();
        InputStream serverStreamRead = new PipedInputStream(serverStreamWrite);
        AdrenalineLauncherServer server = new AdrenalineLauncherServer(true, 10005);
        server.setInputStream(serverStreamRead);
        (new Thread(() -> tryDo(() -> server.start()))).start();
        Thread.sleep(300);
        for(int i=0; i < 5; i++) {
            (new Thread(() -> Bot.login(Bot.getLoginString(++j), true, "127.0.0.1", 10005))).start();
            Thread.sleep(1000);
        }
        j = 0;
        Thread.sleep(3000);
    }

    @Test
    void mainStart() throws InterruptedException {
        if(!GUI_TEST)
            return;

        (new Thread(() -> AdrenalineLauncherServer.main(new String[]{}))).start();
        Thread.sleep(300);

        /*
        for(int i=0; i < 1; i++) {
            (new Thread(() -> Bot.login(Bot.getLoginString(++j), true, "127.0.0.1", 21508))).start();
            Thread.sleep(1000);
        }

         */
        AdrenalineLauncherClient.main(new String[]{});
        Thread.sleep(200000);
    }

    @Test
    void callbackServer() throws Exception
    {
        if(!REMOTE_TESTING)
            return;

        RMIListener<AdrenalineServerRMI, ICallbackAdrenalineClient> listener = new RMIListener<>(1099, () -> new AdrenalineServerRMI(new Controller()));
        listener.start();
        listener.newClientEvent.addEventHandler((a, rmiServer) -> {
            rmiServer.server().initialize(rmiServer.client());
            try {
                rmiServer.client().modelChanged(null);
            }
            catch(RemoteException e){}
        });

        while(listener.getConnected().isEmpty())
            ;
        listener.stop();
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
