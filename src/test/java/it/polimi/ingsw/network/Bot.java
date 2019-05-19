package it.polimi.ingsw.network;

import java.io.InputStream;
import java.io.OutputStream;

public class Bot
{
    public static void login(InputStream in, OutputStream out, int delay) {
        String login = "turangla lella\n";
        (new Thread(() -> {
            try {
                Thread.sleep(delay );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("turanga_lella\n\n");
        })).start();
    }
}
