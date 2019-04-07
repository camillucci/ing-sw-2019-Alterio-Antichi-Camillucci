package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public class AmmoDeck {

    private ArrayList<AmmoCard> deck = new ArrayList<>();
    private ArrayList<AmmoCard> discarded = new ArrayList<>();
    private Random rand = new Random();

    public AmmoDeck() {
        IntStream.range(0, 3).forEach(i -> {
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
        });
    }

    public AmmoCard draw() {
        if(this.isEmpty()) {
            deck = discarded;
        }
        return deck.remove(rand.nextInt(deck.size()));
    }

    public void addDiscarded(AmmoCard discardedCard) {
        discarded.add(discardedCard);
    }

    private boolean isEmpty() {
        return deck.isEmpty();
    }
}
