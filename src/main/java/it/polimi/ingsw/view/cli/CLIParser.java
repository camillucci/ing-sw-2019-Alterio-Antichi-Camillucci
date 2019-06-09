package it.polimi.ingsw.view.cli;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Predicate;

/**
 * this class reads inputs and makes sure that they're appropriate to the context
 */

public class CLIParser {
    /**
     *
     */
    public static final CLIParser parser = new CLIParser(System.in);
    /**
     * reader is used to acquire inputs from the input-line
     */
    private BufferedReader reader;

    /**
     * creates the reader
     * @param inputStream
     */
    public CLIParser(InputStream inputStream){
        reader =  new BufferedReader(new InputStreamReader(inputStream));
    }

    public void changeInputStream(InputStream inputStream){
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    /**
     * Gets user's name of choice, making sure it's length is within 2 and 16 characters
     * @return user's name
     * @throws IOException
     */
    public String parseName() throws IOException {
        return getStringIf(name -> name.length() >= 2 && name.length() <= 16);
    }

    /**
     * Gets user's choice, which has to be either 0 or 1. In case it isn't, asks again.
     * @return user's choice between 0 and 1.
     * @throws IOException
     */
    public int parseChoice() throws IOException {
        return getIntIf(choice -> choice == 0 || choice == 1);
    }

    /**
     * Gets user's choice via input and checks that it is valid, which means it has to be a number and has to be
     * between 0 and index - 1. In case the answer isn't acceptable, asks again.
     * @param index maximum number accepted as answer from the user
     * @return user's answer once it's correct
     * @throws IOException
     */
    public int parseIndex(int index) throws IOException {
        return getIntIf(answer -> answer >= 0 && answer < index);
    }

    /**
     * Gets user's gamelencth of choice, making sure it is between 5 and 8. If it isn't, user is asked one more time.
     * @return user's game length of choice, once it's acceptable.
     * @throws IOException
     */
    public int parseGameLength() throws IOException {
        return getIntIf(answer -> answer >= 5 && answer <= 8);
    }

    /**
     * Gets user's map type of choice, which is indicated with a number between 0 and 3. If user's choice was
     * unacceptable, the map type is asked again.
     * @return User's map type of choice, once it's acceptable.
     * @throws IOException
     */
    public int parseGameMap() throws IOException {
        return getIntIf(answer -> answer >= 0 && answer <= 3);
    }

    /**
     * Gets user's action of choice as input, making sure that the answer is acceptable. If it isn't, user is asked
     * again.
     * @param index maximum number user can choose as an answer
     * @return user's choice, once it's acceptable.
     * @throws IOException
     */
    public int parseActions(int index) throws IOException {
        return getIntIf(answer -> answer >= 0 && answer <= index);
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
