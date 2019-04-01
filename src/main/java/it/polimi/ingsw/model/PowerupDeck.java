package it.polimi.ingsw.model;

import java.util.ArrayList;

public class PowerupDeck {
    private int numberOfCards;
    private ArrayList<PowerupCard> deck;
    private ArrayList<PowerupCard> discarded;

    public PowerupDeck() {
        this.numberOfCards = 24;
        this.deck = new ArrayList<>();
        for(int i = 0; i<21; i++)
            deck.add(new PowerupCard()); // Problem: how to add all cards
        this.discarded = new ArrayList<>();
    }

    public PowerupCard draw() {
        //TODO
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }
}
