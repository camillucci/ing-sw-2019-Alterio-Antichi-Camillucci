package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;

public class WeaponDeck {

    private ArrayList<WeaponCard> deck = new ArrayList<>();

    public WeaponDeck() {
        //TODO
        //deck = (ArrayList<WeaponCard>) WeaponFactory.getWeapons();
        Collections.shuffle(deck);
    }

    public WeaponCard draw() {
        if(deck.isEmpty())
            return null;
        return deck.remove(0);
    }
}
