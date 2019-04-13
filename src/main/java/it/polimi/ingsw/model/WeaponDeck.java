package it.polimi.ingsw.model;

import java.util.Collections;
import java.util.List;

public class WeaponDeck {

    private List<WeaponCard> deck;

    public WeaponDeck() {
        deck = WeaponFactory.getWeapons();
        Collections.shuffle(deck);
    }

    public WeaponCard draw() {
        if(deck.isEmpty())
            return null;
        return deck.remove(0);
    }
}
