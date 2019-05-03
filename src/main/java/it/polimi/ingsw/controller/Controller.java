package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.action.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    MatchManager matchManager;
    private List<Room> lobby = new ArrayList<>();

    public Controller() {
        this.matchManager = new MatchManager();
    }

    public void handleAction(Action action) {
        matchManager.handleAction(action);
        //TODO update all players view
    }

    private Room newRoom() {
        Room room = new Room();
        lobby.add(room);
        return room;
    }

    private Room newRoom(int gameLength, int gameSize)
    {
        Room ret = new Room(gameLength, gameSize);
        lobby.add(ret);
        return ret;
    }

    public List<String> getPlayerNames()
    {
        return lobby.stream().flatMap(room -> room.getPlayerNames().stream()).collect(Collectors.toList());
    }

    public boolean existName(String name)
    {
        for(String s : getPlayerNames())
            if(s.equals(name))
                return true;
        return false;
    }

    public Room getAvailableRoom() {
        if(lobby.get(lobby.size() - 1).getAvailableSeats())
            return lobby.get(lobby.size() - 1);
        newRoom();
        return lobby.get(lobby.size()-1);
    }
}
