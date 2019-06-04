package it.polimi.ingsw.view.cli;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CLIParser {
    public static final CLIParser parser = new CLIParser(System.in);
    private BufferedReader reader;
    private static final String NUMBER_FORMAT_EXCEPTION = "Please insert a number";

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
        int choice = -1;
        do {
            try {
                choice = Integer.parseInt(reader.readLine());
            }
            catch(NumberFormatException e) {
                //CLIMessenger.printMessage(NUMBER_FORMAT_EXCEPTION);
            }
        } while(choice != 0 && choice != 1);
        return choice;
    }

    public int parseIndex(int index) throws IOException {
        int answer = -1;
        do {
            try {
                answer = Integer.parseInt(reader.readLine());
            }
            catch(NumberFormatException e) {
                CLIMessenger.printMessage(NUMBER_FORMAT_EXCEPTION);
            }
        } while(answer < 0 || answer >= index);
        return answer;
    }

    public void parseActions() {
        //TODO
    }

    public int parseGameLength() throws IOException {
        int answer = -1;
        do {
            try {
                answer = Integer.parseInt(reader.readLine());
            }
            catch(NumberFormatException e) {
                //CLIMessenger.printMessage(NUMBER_FORMAT_EXCEPTION);
            }
        } while(answer < 5 || answer > 8);
        return answer;
    }

    public int parseGameMap() throws IOException {
        int answer = -1;
        do {
            try {
                answer = Integer.parseInt(reader.readLine());
            }
            catch(NumberFormatException e) {
                //CLIMessenger.printMessage(NUMBER_FORMAT_EXCEPTION);
            }
        } while(answer < 0 || answer > 3);
        return answer;
    }
}
