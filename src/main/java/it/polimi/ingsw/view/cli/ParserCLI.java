package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.PlayerColor;

public class ParserCLI {

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

    public PlayerColor parseColor() {
        //TODO
        return null;
    }

}
