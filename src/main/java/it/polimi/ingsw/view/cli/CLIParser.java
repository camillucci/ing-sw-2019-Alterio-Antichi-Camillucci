package it.polimi.ingsw.view.cli;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Predicate;

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
        return getStringIf(name -> name.length() < 2 || name.length() > 16);
    }

    public int parseChoice() throws IOException {
        return getIntIf(choice -> choice != 0 && choice != 1);
    }

    public int parseIndex(int index) throws IOException {
        return getIntIf(answer -> answer < 0 || answer >= index);
    }

    public int parseGameLength() throws IOException {
        return getIntIf(answer -> answer < 5 || answer > 8);
    }

    public int parseGameMap() throws IOException {
        return getIntIf(answer -> answer < 0 || answer > 3);
    }

    public int parseActions(int index) throws IOException {
        return getIntIf(answer -> answer < 0 || answer > index);
    }

    private int getIntIf(Predicate<Integer> inputTest) throws IOException {
        int val = Integer.parseInt(reader.readLine());
        while (!inputTest.test(val)) {
            CLIMessenger.incorrectInput();
            val = Integer.parseInt(reader.readLine());
        }
        return val;
    }

    private String getStringIf(Predicate<String> inputTest) throws IOException {
        String val = reader.readLine();
        while (!inputTest.test(val)) {
            CLIMessenger.incorrectInput();
            val = reader.readLine();
        }
        return val;
    }

}
