package it.polimi.ingsw.model;

import java.util.ArrayList;

public class WeaponDeck {
    private int numberOfCards;
    private ArrayList<WeaponCard> deck;
    private ArrayList<WeaponCard> discarded;

    public WeaponDeck() {
        this.numberOfCards = 21;
        this.deck = new ArrayList<WeaponCard>();
        for(int i = 0; i<21; i++)
            deck.add(new WeaponCard());
        this.discarded = new ArrayList<WeaponCard>();
    }
}
