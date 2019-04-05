package it.polimi.ingsw.model;

import it.polimi.ingsw.model.weapons.*;

import java.util.ArrayList;
import java.util.Random;

public class WeaponDeck {

    private int numberOfCards;
    private ArrayList<WeaponCard> deck = new ArrayList<>();
    private Random rand = new Random();

    public WeaponDeck() {
        deck.add(new WeaponLockRifle());
        deck.add(new WeaponMachineGun());
        //TODO Add all cards
        this.numberOfCards = deck.size();
    }

    public WeaponCard draw() {
        if(this.isEmpty()) {
            return null; //TODO
        }
        numberOfCards--;
        return deck.remove(rand.nextInt(deck.size()));
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }
}
