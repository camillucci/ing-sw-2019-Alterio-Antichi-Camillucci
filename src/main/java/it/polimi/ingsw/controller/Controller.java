package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.PlayerColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    private List<Room> lobby = new ArrayList<>();
    private List<String> joiningPlayers = new ArrayList<>();
    public Controller(){
        newRoom();
    }

    private Room newRoom() {
        Room room = new Room();
        room.newPlayerEvent.addEventHandler((a, name) -> joiningPlayers.remove(name));
        lobby.add(room);
        return room;
    }

    public synchronized Room getAvailableRoom() {
        Room ret = lobby.get(lobby.size()-1);
        if(ret.isJoinable())
            return ret;
        return newRoom();
    }

    public synchronized boolean newPlayer(String name) {
        for (Room room : lobby)
            if (room.getPlayerNames().contains(name))
                return false;
        if(joiningPlayers.contains(name))
            return false;
        joiningPlayers.add(name);
        return true;
    }


}
