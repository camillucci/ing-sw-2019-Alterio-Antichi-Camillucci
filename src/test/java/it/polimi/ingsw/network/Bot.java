package it.polimi.ingsw.network;

import it.polimi.ingsw.AdrenalineLauncherClient;
import it.polimi.ingsw.view.cli.CLIMessenger;
import it.polimi.ingsw.view.cli.CLIParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.NotBoundException;

public class Bot
{
    static int i=0;
    private static String getLoginString(int playerNum){
        StringBuilder actionInputString = new StringBuilder();
        int N = 10;
        for(int i=0; i < N; i++)
            actionInputString.append("0\n");

        if(playerNum == 1)
            return "0\nturangla_lella"+ i++ +"\n0\n6\n0\n0" + actionInputString.toString();
        else
            return "0\nturanga_lella" + i++ + "\n0\n0\n" + actionInputString.toString();
    }
    public static void login(boolean socket, String serverName, int port, int playerNum) {
        try {
            CLIParser.parser.changeInputStream(new ByteArrayInputStream(getLoginString(playerNum).getBytes()));
            AdrenalineLauncherClient client = new AdrenalineLauncherClient(false, socket, serverName, port);
            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
