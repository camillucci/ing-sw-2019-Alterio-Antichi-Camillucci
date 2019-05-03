package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.network.AdrenalineServer;

import java.util.ArrayList;
import java.util.List;

public class Room
{
    private List<AdrenalineServer> clients = new ArrayList<>();
    private List<PlayerColor> colors = new ArrayList<>();
    private List<String> playerNames = new ArrayList<>();
    private int gameLength;
    private int gameSize;
    private Match match;

    public Room() {}

    public Room(int gamelength, int gamesize)
    {
        this.gameLength = gamelength;
        this.gameSize = gamesize;
    }

    public boolean addPlayer(PlayerColor color, String playerName){
        colors.add(color);
        playerNames.add(playerName);
        if(playerNames.size() == 5)
            match = new Match(playerNames, colors, gameLength, gameSize);
        else if(playerNames.size() == 1)
            return true;
        return false;
    }

    private void newMatch()
    {
        match = new Match(playerNames, colors, gameLength, gameSize);
    }

    public List<String> getPlayerNames(){
        return new ArrayList<>(playerNames);
    }

    public List<PlayerColor> getPlayerColors() { return colors;}

    public boolean getAvailableSeats() {
        if(playerNames.size() < 5)
            return true;
        return false;
    }

    public void setGameSize(int gameSize) {
        this.gameSize = gameSize;
    }

    public void setGameLength(int gameLength) {
        this.gameLength = gameLength;
    }
}
