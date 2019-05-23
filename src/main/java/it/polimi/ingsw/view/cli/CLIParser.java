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
        String name = reader.readLine();
        //TODO add restrictions
        return name;
    }

    public int parseChoice() throws IOException {
        int choice = Integer.parseInt(reader.readLine());
        if (choice == 0 || choice == 1)
            return choice;
        return -1;
    }

    public int parseIndex(int index) throws IOException {
        int answer = Integer.parseInt(reader.readLine());
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
