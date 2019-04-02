package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

public class PowerupDeck {
    private int numberOfCards;
    private ArrayList<PowerupCard> deck = new ArrayList<>();
    private ArrayList<PowerupCard> discarded = new ArrayList<>();
    private Random rand = new Random();

    public PowerupDeck() {
        this.numberOfCards = 24;
        //TODO Add all cards
    }

    public PowerupCard draw() {
        if(this.isEmpty()) {
            deck = discarded;
            numberOfCards = deck.size();
        }
        numberOfCards--;
        return deck.remove(rand.nextInt(deck.size()));
    }

    public boolean isEmpty() { return deck.isEmpty(); }
}
