package it.polimi.ingsw.model;

import it.polimi.ingsw.model.weapons.*;

import java.util.ArrayList;
import java.util.Random;

public class WeaponDeck {

    private ArrayList<WeaponCard> deck = new ArrayList<>();
    private Random rand = new Random();

    public WeaponDeck() {
        deck.add(new LockRifle());
        deck.add(new MachineGun());
        //TODO Add all cards
    }

    public WeaponCard draw() {
        if(deck.isEmpty()) {
            return null;
        }
        return deck.remove(rand.nextInt(deck.size()));
    }
}
