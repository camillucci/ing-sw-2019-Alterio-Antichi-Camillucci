package it.polimi.ingsw.network;
import it.polimi.ingsw.AdrenalineLauncherClient;
import org.junit.jupiter.api.Test;

import java.io.*;


class LoginTest {
    private PipedOutputStream pipedOutputStream = new PipedOutputStream();
    private final boolean GUI_TEST = false;

    void stop()
    {
        try {
            PrintStream stream = new PrintStream(pipedOutputStream);
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
}
