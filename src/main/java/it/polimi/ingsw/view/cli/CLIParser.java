package it.polimi.ingsw.view.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class CLIParser {
    private CLIParser(){}
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void setInputStream(InputStream inputStream){
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public static String parseName() throws IOException {
        String name = reader.readLine();
        //TODO add restrictions
        return name;
    }

    public static int parseChoice() throws IOException {
        int choice = Integer.parseInt(reader.readLine());
        if (choice == 0 || choice == 1)
            return choice;
        return -1;
    }

    public static int parseIndex(int index) throws IOException {
        int answer = Integer.parseInt(reader.readLine());
        if(answer < index && answer >= 0)
            return answer;
        return -1;
    }

    public static void parseActions() {
        //TODO
    }

    public static int parseGameLenght() {
        int answer = Integer.parseInt(System.in.toString());
        if(answer <= 8 && answer >= 5)
            return answer;
        return -1;
    }

    public static int parseGameMap() {
        int answer = Integer.parseInt(System.in.toString());
        if(answer < 3 && answer >= 0)
            return answer;
        return -1;
    }
}
