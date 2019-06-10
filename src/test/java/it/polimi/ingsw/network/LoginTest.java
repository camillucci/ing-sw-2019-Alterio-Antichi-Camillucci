package it.polimi.ingsw.network;
import it.polimi.ingsw.AdrenalineLauncherClient;
import it.polimi.ingsw.AdrenalineLauncherServer;
import it.polimi.ingsw.generics.Bottleneck;
import it.polimi.ingsw.view.Login;
import it.polimi.ingsw.view.cli.CLIParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;


class LoginTest {
    private PipedOutputStream outstr = new PipedOutputStream();
    private PipedInputStream instr = new PipedInputStream(outstr);
    private final boolean SOCKET_TESTING = false;
    private final boolean REMOTE_TESTING = false;
    private final boolean GUI_TEST = false;
    static int j = 0;
    Bottleneck bottleneck = new Bottleneck();

    public LoginTest() throws IOException {
        AdrenalineLauncherServer.setParser(new CLIParser(instr));
    }

    void stop()
    {
        try {
            PrintStream stream = new PrintStream(outstr);
            stream.print("1\n");
            Thread.sleep(1000);
        }
        catch(Exception e) {}
    }

    @Test
    void test2() throws InterruptedException {
        if(!GUI_TEST)
            return;
        AdrenalineLauncherClient.main(new String[]{});
        System.exit(0);
        Thread.sleep(200000);
        stop();
    }
    @Test
    void test32() throws InterruptedException {
        if(!GUI_TEST)
            return;
        AdrenalineLauncherClient.main(new String[]{});
        Thread.sleep(200000);
        stop();
    }

    @Test
    void mainStart() throws InterruptedException {
        if(GUI_TEST)
            return;

        (new Thread(() -> AdrenalineLauncherServer.main(new String[]{}))).start();
        Thread.sleep(300);

        //AdrenalineLauncherClient.main(new String[]{});

        for(int h = 0; h < 1; h++, j=0)
            for (int i = 0; i < 3; i++) {
                (new Thread(() -> Bot.login(true, "127.0.0.1", 9999, ++j))).start();
                Thread.sleep(1000);
            }
        //stop();
    }
}
