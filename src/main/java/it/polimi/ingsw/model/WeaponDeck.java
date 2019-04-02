package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

public class WeaponDeck {
    private int numberOfCards;
    private ArrayList<WeaponCard> deck = new ArrayList<>();
    private ArrayList<WeaponCard> discarded = new ArrayList<>();
    private Random rand = new Random();

    public WeaponDeck() {
        this.numberOfCards = 21;
        //TODO Add all cards
    }

    public WeaponCard draw() { return deck.remove(rand.nextInt(deck.size())); }

    public boolean isEmpty() {
        return deck.isEmpty();
    }
}
