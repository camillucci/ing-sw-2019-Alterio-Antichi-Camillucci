package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.PlayerColor;
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
    private Match match = null;
    private MatchManager matchManager;

    public Room() {

        for (PlayerColor pc : PlayerColor.values()) {
            availableColors.add(pc);
        }
    }

    public void handleAction(Action action) {
        matchManager.handleAction(action);
        //TODO update all players view
    }

    public boolean addPlayer(int index, String playerName, AdrenalineServer client) throws Exception {
        clients.add(client);
        playerColors.add(availableColors.get(index));
        availableColors.remove(index);
        playerNames.add(playerName);
        if(playerNames.size() == 5)
            match = newMatch();
        else if(playerNames.size() == 1)
            return true;
        else if(playerNames.size() == 3)
            threePlayers();
        return false;
    }

    private Match newMatch() throws Exception {
        for(AdrenalineServer client : clients) {
            client.matchStart();
        }
        match = new Match(playerNames, playerColors, gameLength, gameSize);
        matchManager = new MatchManager(match, this);
        return match;
    }

    public List<String> getPlayerNames(){
        return new ArrayList<>(playerNames);
    }

    public List<String> getAvailableColors() {
        ArrayList<String> colors = new ArrayList<>();
        for (PlayerColor pc : availableColors)
            colors.add(pc.name());
        return colors;
    }

    public boolean getAvailableSeats() {
        return playerNames.size() < 5;
    }

    private void threePlayers() throws Exception {
        //TODO add timer
        if(match == null)
            match = newMatch();
    }

    public void setGameSize(int gameSize) {
        this.gameSize = gameSize;
    }

    public void setGameLength(int gameLength) {
        this.gameLength = gameLength;
    }

    public void sendActions(List<Action> actions, int client) {
        //TODO send actions to client
    }
}
