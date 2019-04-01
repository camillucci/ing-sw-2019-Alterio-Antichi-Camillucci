package it.polimi.ingsw.model;

import java.util.ArrayList;

public class WeaponDeck {
    private int numberOfCards;
    private ArrayList<WeaponCard> deck;
    private ArrayList<WeaponCard> discarded;

    public WeaponDeck() {
        this.numberOfCards = 21;
        this.deck = new ArrayList<>();
        for(int i = 0; i<21; i++) {
            deck.add(new WeaponCard()); // Problem: how to add all cards
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
