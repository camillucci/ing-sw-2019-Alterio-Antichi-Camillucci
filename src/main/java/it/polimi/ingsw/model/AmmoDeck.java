package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

public class AmmoDeck {

    private ArrayList<AmmoCard> deck = new ArrayList<>();
    private ArrayList<AmmoCard> discarded = new ArrayList<>();
    private Random rand = new Random();
    private static final int COPY_OF_CARDS = 3;

    public AmmoDeck() {
        for (int i = 0; i < COPY_OF_CARDS; i++) {
            deck.add(new AmmoCard(0, 1, 2, false));
            deck.add(new AmmoCard(0, 2, 1, false));
            deck.add(new AmmoCard(1, 0, 2, false));
            deck.add(new AmmoCard(1, 2, 0, false));
            deck.add(new AmmoCard(2, 0, 1, false));
            deck.add(new AmmoCard(2, 1, 0, false));
            deck.add(new AmmoCard(0, 0, 2, true));
            deck.add(new AmmoCard(0, 1, 1, true));
            deck.add(new AmmoCard(0, 2, 0, true));
            deck.add(new AmmoCard(1, 0, 1, true));
            deck.add(new AmmoCard(1, 1, 0, true));
            deck.add(new AmmoCard(2, 0, 0, true));
        }
    }

    public AmmoCard draw() {
        if(deck.isEmpty()) {
            deck = discarded;
        }
        return deck.remove(rand.nextInt(deck.size()));
    }

    public void addDiscarded(AmmoCard discardedCard) {
        discarded.add(discardedCard);
    }
}
