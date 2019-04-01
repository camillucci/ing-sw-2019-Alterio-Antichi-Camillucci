package it.polimi.ingsw.model;

import java.util.ArrayList;

public class AmmoDeck {
    private int numberOfCards;
    private ArrayList<AmmoCard> deck;
    private ArrayList<AmmoCard> discarded;

    public AmmoDeck() {
        this.numberOfCards = 21;
        this.deck = new ArrayList<>();
        for(int i = 0; i<21; i++) {
            deck.add(new AmmoCard()); // Problem: how to add all cards
        }
        this.discarded = new ArrayList<>();
    }

    public WeaponCard draw() {
        //TODO
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }
}
