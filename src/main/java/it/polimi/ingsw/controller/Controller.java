package it.polimi.ingsw.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    private List<Room> lobby = new ArrayList<>();

    public Controller(){
        newRoom();
    }

    private void newRoom() {
        Room room = new Room();
        lobby.add(room);
        room.matchStartedEvent.addEventHandler((a,b) -> newRoom());
    }

    private List<String> getPlayerNames() {
        return lobby.stream().flatMap(room -> room.getPlayerNames().stream()).collect(Collectors.toList());
    }

    public boolean existName(String name) {
        for (String s : getPlayerNames())
            if (s.equals(name))
                return true;
        return false;
    }

    public Room getAvailableRoom() {
        return lobby.get(lobby.size()-1);
    }
}
