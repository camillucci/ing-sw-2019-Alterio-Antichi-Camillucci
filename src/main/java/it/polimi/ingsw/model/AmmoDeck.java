package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

public class AmmoDeck {
    private int numberOfCards;
    private ArrayList<AmmoCard> deck = new ArrayList<>();
    private ArrayList<AmmoCard> discarded = new ArrayList<>();
    private Random rand = new Random();

    public AmmoDeck() {
        this.numberOfCards = 21;
        //TODO Add all cards
    }

    public AmmoCard draw() { return deck.remove(rand.nextInt(deck.size())); }

    public boolean isEmpty() {
        return deck.isEmpty();
    }
}
