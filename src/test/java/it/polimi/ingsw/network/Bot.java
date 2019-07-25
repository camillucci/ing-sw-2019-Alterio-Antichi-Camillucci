package it.polimi.ingsw.network;

import it.polimi.ingsw.AdrenalineLauncherClient;
import it.polimi.ingsw.view.cli.CLIParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bot
{
    private static int i=0;
    private static Logger logger = Logger.getLogger("Bot");

    private static String getLoginString(int playerNum){
        StringBuilder actionInputString = new StringBuilder();
        int N = 20;
        actionInputString.append("0\n".repeat(N));

        if(playerNum == 1)
            return "0\nJohn"+ i++ +"\n0\n6\n0\n0" + actionInputString.toString();
        else
            return "0\nJames" + i++ + "\n0\n0\n" + actionInputString.toString();
    }

    public static void login(boolean socket, String serverName, int port, int playerNum) {
        try {
            CLIParser.parser.changeInputStream(new ByteArrayInputStream(getLoginString(playerNum).getBytes()));
            AdrenalineLauncherClient client = new AdrenalineLauncherClient(false, socket, serverName, port);
            client.start();
        } catch (IOException | NotBoundException | InterruptedException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }
}
