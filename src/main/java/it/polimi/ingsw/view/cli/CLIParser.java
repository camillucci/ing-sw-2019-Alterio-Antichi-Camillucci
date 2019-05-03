package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;

import java.util.ArrayList;

public class CLIParser {

    public String parseName() {
        String name = System.in.toString();
        //TODO add restrictions
        return name;
    }

    public boolean parseChoice() {
        String type = System.in.toString();
        if(type == "1")
            return true;
        else
            return false;

        //TODO catch exceptions
    }

    public int parseColor(ArrayList<String> availableColors) {
        int answer = Integer.parseInt(System.in.toString());
        if(answer < availableColors.size() && answer >= 0)
            return answer;
        //TODO catch exception
        return -1;
    }

    public int parseGameLenght() {
        int answer = Integer.parseInt(System.in.toString());
        if(answer <= 8 && answer >= 5)
            return answer;
        //TODO catch exceptions
        return 0;
    }

    public int parseGameMap() {
        int answer = Integer.parseInt(System.in.toString());
        if(answer < 3 && answer >= 0)
            return answer;
        //TODO catch exceptions
        return 0;
    }

    public void parseTargets() {
        //TODO
    }

}
