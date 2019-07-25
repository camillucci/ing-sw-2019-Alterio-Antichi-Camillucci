package it.polimi.ingsw.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest
{
    @Test
    void roomTest() throws Room.MatchStartingException, Room.NotAvailableColorException {
        Room room = new Room(3, 4);
        room.addPlayer("BLUE", "John");
        assertEquals(room.getPlayerNames().size(), 1);
        assertEquals(1,room.getId());
        assertFalse(room.isHost("player"));
        System.out.println(room.getAvailableColors());
    }

    @Test
    void controller(){
        Controller controller = new Controller(3, 4);
        assertNull(controller.checkReconnected("James"));
        controller.newPlayer("player");
        assertNotNull(controller.getAvailableRoom());
    }
}