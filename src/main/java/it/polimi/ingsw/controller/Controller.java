package it.polimi.ingsw.controller;

import java.util.ArrayList;
import java.util.List;

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

    public synchronized void notifyPlayerDisconnected(String name){
        if(name == null) // name not registered
            return;
        if(joiningPlayers.contains(name))
            joiningPlayers.remove(name);
        else
            for(Room room : lobby)
                if(room.getPlayerNames().contains(name)) {
                    room.notifyPlayerDisconnected(name);
                    return;
                }
        throw new RuntimeException("disconnected player doesn't exist");
    }
}
