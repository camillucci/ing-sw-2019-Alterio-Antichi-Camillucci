package it.polimi.ingsw.network;

import it.polimi.ingsw.AdrenalineLauncherClient;
import it.polimi.ingsw.view.cli.CLIParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.NotBoundException;

public class Bot
{
    static int i=0;
    public static String getLoginString(int playerNum){
        if(playerNum == 1)
            return "0\nturangla_lella"+ i++ +"\n0\n6\n0\n";
        else
            return "0\nturanga_lella" + i++ + "\n0\n";
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
