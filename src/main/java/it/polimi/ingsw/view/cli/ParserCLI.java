package it.polimi.ingsw.view.cli;

import java.io.InputStream;

public class ParserCLI {
    private ManagerCLI manager;

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

}
