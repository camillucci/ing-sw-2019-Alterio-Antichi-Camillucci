package it.polimi.ingsw.controller;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.PlayerColor;
import java.util.*;

public class Room
{
    public final IEvent<Room, Match> matchStartedEvent = new Event<>();
    public final IEvent<Room, Integer> timerStartEvent = new Event<>();
    public final IEvent<Room, Integer> timerTickEvent = new Event<>();
    public final IEvent<Room, Integer> timerStopEvent = new Event<>();
    public final IEvent<Room, String> newPlayerEvent = new Event<>();
    private final int TIMEOUT = 5;
    private final int PERIOD = 1;
    private List<PlayerColor> playerColors = new ArrayList<>();
    private List<PlayerColor> availableColors = new ArrayList<>();
    private List<String> playerNames = new ArrayList<>();
    private int readyCounter = 0;
    private int gameLength;
    private int gameSize;
    private Match match = null;
    private MatchManager matchManager;
    private RoomTimer timer = new RoomTimer(TIMEOUT, PERIOD);

    public Room() {
        availableColors.addAll(Arrays.asList(PlayerColor.values()));
        setupEvents();
    }

    private void setupEvents()
    {
        timer.timerTickEvent.addEventHandler((a, timeElapsed) -> ((Event<Room, Integer>)timerTickEvent).invoke(this, TIMEOUT - timeElapsed));
        timer.timeoutEvent.addEventHandler((a, b) -> startMatch());
    }

    public void startMatch(){
        match = new Match(playerNames, playerColors, gameLength, gameSize);
        matchManager = new MatchManager(match, this);
        ((Event<Room, Match>)matchStartedEvent).invoke(this, match);
    }

    public synchronized boolean addPlayer(int index, String playerName) {
        if(match != null)
            return false;

        playerColors.add(availableColors.get(index));
        availableColors.remove(index);
        playerNames.add(playerName);
        newPlayer(playerName);
        return playerNames.size() == 1;
    }

    private void newPlayer(String name){
        ((Event<Room, String>)newPlayerEvent).invoke(this, name);
    }

    public synchronized List<String> getPlayerNames(){
        return new ArrayList<>(playerNames);
    }

    public synchronized void notifyPlayerReady()
    {
        if (++readyCounter == 3) {
            timer.start();
            ((Event<Room, Integer>) timerStartEvent).invoke(this, TIMEOUT);
        } else if (readyCounter == 5) {
            timer.stop();
            startMatch();
        }
    }
    public synchronized List<String> getAvailableColors() {
        ArrayList<String> colors = new ArrayList<>();
        for (PlayerColor pc : availableColors)
            colors.add(pc.name());
        return colors;
    }

    public synchronized boolean getAvailableSeats() {
        return playerNames.size() < 5;
    }

    public void setGameLength(int gameLength) {
        this.gameLength = gameLength;
    }

    public void setGameSize(int gameSize) {
        this.gameSize = gameSize;
    }
}
