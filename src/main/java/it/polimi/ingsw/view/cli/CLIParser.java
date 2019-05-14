package it.polimi.ingsw.view.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CLIParser {
    CLIMessenger messenger;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public CLIParser(CLIMessenger messenger) {
        this.messenger = messenger;
    }

    public String parseName() {
        String name = System.in.toString();
        //TODO add restrictions
        return name;
    }

    public int parseChoice() throws IOException {
        int choice = Integer.parseInt(reader.readLine());
        if (choice == 0 || choice == 1)
            return choice;
        return -1;
    }

    public int parseIndex(int index) {
        int answer = Integer.parseInt(System.in.toString());
        if(answer < index && answer >= 0)
            return answer;
        return -1;
    }

    public void parseActions() {
        //TODO
    }

    public int parseGameLenght() {
        int answer = Integer.parseInt(System.in.toString());
        if(answer <= 8 && answer >= 5)
            return answer;
        return -1;
    }

    public int parseGameMap() {
        int answer = Integer.parseInt(System.in.toString());
        if(answer < 3 && answer >= 0)
            return answer;
        return -1;
    }
}
