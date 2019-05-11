package it.polimi.ingsw.controller;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.AdrenalineServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Room
{
    public final IEvent<Room, Match> matchStartedEvent = new Event<>();
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

    public void handleAction(int index) throws Exception {
        matchManager.handleAction(index);
    }

    public boolean addPlayer(int index, String playerName, AdrenalineServer client) throws IOException {
        if(match != null)
            return false;

        playerColors.add(availableColors.get(index));
        availableColors.remove(index);
        playerNames.add(playerName);
        if(playerNames.size() == 5)
            newMatch();
        else if(playerNames.size() == 1)
            return true;
        else if(playerNames.size() == 3)
            threePlayers();
        return false;
    }

    private void newMatch() throws IOException {
        match = new Match(playerNames, playerColors, gameLength, gameSize);
        matchManager = new MatchManager(match, this);
        ((Event<Room, Match>)matchStartedEvent).invoke(this, match);
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

    private void threePlayers() throws IOException {
        //todo add timer
            if(match == null)
                newMatch();
    }

    public void setGameSize(int gameSize) {
        this.gameSize = gameSize;
    }

    public void setGameLength(int gameLength) {
        this.gameLength = gameLength;
    }


    public void updateView(MatchSnapshot snapshot, int index) throws Exception {
       // clients.get(index).updateView(snapshot);
    }
}
