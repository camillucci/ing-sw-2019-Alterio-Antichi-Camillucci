package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.PowerUpCard;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.network.AdrenalineServer;

import java.util.ArrayList;
import java.util.List;

public class Room
{
    private List<AdrenalineServer> clients = new ArrayList<>();
    private List<PlayerColor> playerColors = new ArrayList<>();
    private List<PlayerColor> availableColors = new ArrayList<>();
    private List<String> playerNames = new ArrayList<>();
    private int gameLength;
    private int gameSize;
    private Match match;
    private MatchManager matchManager;

    public Room() {
        /*
        for (PlayerColor pc : PlayerColor) {
            availableColors.add(pc);
        }

         */
    }

    public Room(int gamelength, int gamesize)
    {
        this.gameLength = gamelength;
        this.gameSize = gamesize;
    }

    public void handleAction(Action action) {
        matchManager.handleAction(action);
        //TODO update all players view
    }

    public boolean addPlayer(int index, String playerName, AdrenalineServer client){
        clients.add(client);
        playerColors.add(availableColors.get(index));
        availableColors.remove(index);
        playerNames.add(playerName);
        if(playerNames.size() == 5)
            match = new Match(playerNames, playerColors, gameLength, gameSize);
        else if(playerNames.size() == 1)
            return true;
        return false;
    }

    private void newMatch()
    {
        match = new Match(playerNames, playerColors, gameLength, gameSize);
    }

    public List<String> getPlayerNames(){
        return new ArrayList<>(playerNames);
    }

    public List<String> getAvailableColors() {
        ArrayList<String> colors = new ArrayList<String>();
        for (PlayerColor pc : availableColors)
            colors.add(pc.name());
        return colors;
    }

    public boolean getAvailableSeats() {
        if(playerNames.size() < 5)
            return true;
        return false;
    }

      public void spawn() throws Exception {
        for(int i = 0; i < clients.size(); i++) {
            ArrayList<PowerUpCard> temp = new ArrayList<>();
            temp.add(match.getPowerUpDeck().draw());
            temp.add(match.getPowerUpDeck().draw());
            ArrayList<String> tempString = new ArrayList<>();
            for (int j = 0; j < temp.size(); j++) {
                tempString.add(temp.get(j).getName());
                tempString.add(temp.get(j).getColor().name());
            }
            int choice = clients.get(i).spawn(tempString);
            //TODO discard temp.get(choice);
            //TODO add the other card to player's card pool
        }
    }

    public void setGameSize(int gameSize) {
        this.gameSize = gameSize;
    }

    public void setGameLength(int gameLength) {
        this.gameLength = gameLength;
    }
}
