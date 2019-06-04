package it.polimi.ingsw.view.cli;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CLIParser {
    public static final CLIParser parser = new CLIParser(System.in);
    private BufferedReader reader;

    public CLIParser(InputStream inputStream){
        reader =  new BufferedReader(new InputStreamReader(inputStream));
    }

    public void setInputStream(InputStream inputStream){
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public String parseName() throws IOException {
        String name;
        do {
            name = reader.readLine();
        } while(name.length() < 2 || name.length() > 16);
        return name;
    }

    public int parseChoice() throws IOException {
        int choice;
        do {
            choice = Integer.parseInt(reader.readLine());
        } while(choice != 0 && choice != 1);
        return choice;
    }

    public int parseIndex(int index) throws IOException {
        int answer;
        do {
            answer= Integer.parseInt(reader.readLine());
        } while(answer < 0 || answer >= index);
        return answer;
    }

    public void parseActions() {
        //TODO
    }

    public int parseGameLength() {
        int answer;
        do {
            answer = Integer.parseInt(System.in.toString());
        } while(answer <5 || answer > 8);
        return answer;
    }

    public int parseGameMap() {
        int answer;
        do {
            answer = Integer.parseInt(System.in.toString());
        } while(answer < 0 || answer > 3);
        return answer;
    }
}
