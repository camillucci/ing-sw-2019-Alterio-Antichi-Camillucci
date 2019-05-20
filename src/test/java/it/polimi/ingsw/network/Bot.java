package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.AdrenalineLauncherClient;
import it.polimi.ingsw.view.cli.CLIParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.NotBoundException;

public class Bot
{
    public static String getLoginString(int playerNum){
        if(playerNum == 1)
            return "turangla_lella" + playerNum + "\n3\n5\n10\n";
        else
            return "turanga_lella" + playerNum + "\n0\n";
    }
    public static void login(String input, boolean socket, String serverName, int port) {
        try {
            CLIParser.parser.setInputStream(new ByteArrayInputStream(input.getBytes()));
            AdrenalineLauncherClient client = new AdrenalineLauncherClient(false, socket, serverName, port);
            client.start();
            int a = 2;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
