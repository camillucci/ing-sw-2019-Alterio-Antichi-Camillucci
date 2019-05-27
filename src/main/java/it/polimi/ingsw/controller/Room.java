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
    private static final int TIMEOUT = 10;
    private static final int PERIOD = 1;
    private List<PlayerColor> playerColors = new ArrayList<>();
    private List<PlayerColor> availableColors = new ArrayList<>();
    private List<String> playerNames = new ArrayList<>();
    private int readyCounter = 0;
    private int gameLength;
    private int gameSize;
    private Match match = null;
    private MatchManager matchManager;
    private RoomTimer timer = new RoomTimer(TIMEOUT, PERIOD);
    private String hostName;
    private int pendingPlayers = 0;
    private boolean matchStarting = false;

    public Room() {
        availableColors.addAll(Arrays.asList(PlayerColor.values()));
        setupEvents();
    }

    private void setupEvents()
    {
        timer.timerTickEvent.addEventHandler((a, timeElapsed) -> ((Event<Room, Integer>)timerTickEvent).invoke(this, TIMEOUT - timeElapsed));
        timer.timeoutEvent.addEventHandler((a, b) -> onTimeout());
    }

    private synchronized void onTimeout(){
        matchStarting = true;
        if(pendingPlayers == 0)
            startMatch();
    }

    private synchronized void startMatch(){
        match = new Match(playerNames, playerColors, gameLength, gameSize);
        matchManager = new MatchManager(match, this);
        ((Event<Room, Match>)matchStartedEvent).invoke(this, match);
    }

    public synchronized void addPlayer(String color, String playerName) throws MatchStartingException, NotAvailableColorException {
        if(matchStarting || availableColors.isEmpty())
            throw new MatchStartingException();

        int index = getAvailableColors().indexOf(color);

        if(index == -1)
            throw new NotAvailableColorException();

        pendingPlayers++;
        playerColors.add(availableColors.get(index));
        availableColors.remove(index);
        playerNames.add(playerName);
        if(playerNames.size() == 1)
            hostName = playerName;
    }

    public boolean isHost(String name) {
        return name.equals(hostName);
    }
    private void newPlayer(String name){
        ((Event<Room, String>)newPlayerEvent).invoke(this, name);
    }

    public synchronized List<String> getPlayerNames(){
        return new ArrayList<>(playerNames);
    }

    public synchronized void notifyPlayerReady(String playerName)
    {
        newPlayer(playerName);
        if(--pendingPlayers == 0 && matchStarting) {
            startMatch();
        }
        else if (++readyCounter == 3) {
            timer.start();
            ((Event<Room, Integer>) timerStartEvent).invoke(this, TIMEOUT);
        } else if (readyCounter == 5) {
            timer.stop();
            startMatch();
        }
    }

    public synchronized List<String> getAvailableColors() throws MatchStartingException
    {
        if(availableColors.isEmpty())
            throw new MatchStartingException();

        ArrayList<String> colors = new ArrayList<>();
        for (PlayerColor pc : availableColors)
            colors.add(pc.name());
        return colors;
    }

    public synchronized boolean isJoinable(){
        return !availableColors.isEmpty() && !matchStarting;
    }

    public synchronized void setGameLength(int gameLength) {
        this.gameLength = gameLength;
    }

    public synchronized void setGameSize(int gameSize) {
        this.gameSize = gameSize;
    }

    public class MatchStartingException extends Exception {}
    public class NotAvailableColorException extends Exception {}
}
