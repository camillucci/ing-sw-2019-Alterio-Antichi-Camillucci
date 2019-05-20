package it.polimi.ingsw.view.cli;

import java.io.*;
import java.util.List;

public class CLIParser {
    public final static CLIParser parser = new CLIParser(System.in);
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
        throw new RuntimeException("not valid choice");
    }

    public int parseIndex(int index) throws IOException {
        int answer = Integer.parseInt(reader.readLine());
        if(answer < index && answer >= 0)
            return answer;
        throw new RuntimeException("not valid choice");
    }

    public void parseActions() {
        //TODO
    }

    public int parseGameLenght() {
        int answer = Integer.parseInt(System.in.toString());
        if(answer <= 8 && answer >= 5)
            return answer;
        throw new RuntimeException("not valid choice");
    }

    public int parseGameMap() {
        int answer = Integer.parseInt(System.in.toString());
        if(answer < 3 && answer >= 0)
            return answer;
        throw new RuntimeException("not valid choice");
    }
}
