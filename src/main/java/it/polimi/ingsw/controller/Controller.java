package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.action.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    private List<Room> lobby = new ArrayList<>();

    public Controller(){
    }

    private Room newRoom() {
        Room room = new Room();
        lobby.add(room);
        return room;
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
