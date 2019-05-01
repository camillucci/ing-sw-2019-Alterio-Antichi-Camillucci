package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.PlayerColor;

import java.util.ArrayList;
import java.util.List;

public class Room
{
    private List<PlayerColor> colors = new ArrayList<>();
    private List<String> playerNames = new ArrayList<>();
    private int gameLength;
    private int gameSize;
    private Match match;

    public Room(int gamelength, int gamesize)
    {
        this.gameLength = gamelength;
        this.gameSize = gamesize;
    }
    public void addPlayer(PlayerColor color, String playerName){
        colors.add(color);
        playerNames.add(playerName);
    }

    private void newMatch()
    {
        match = new Match(playerNames, colors, gameLength, gameSize);
    }

    public List<String> getPlayerNames(){
        return new ArrayList<>(playerNames);
    }
}
