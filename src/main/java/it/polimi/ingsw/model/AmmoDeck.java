package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AmmoDeck {

    private List<AmmoCard> deck;
    private List<AmmoCard> discarded = new ArrayList<>();

    public AmmoDeck() {
        deck = CardsFactory.getAmmo();
        Collections.shuffle(deck);
    }

    public AmmoCard draw() {
        if(deck.isEmpty()) {
            deck = discarded;
            discarded = new ArrayList<>();
        }
        return deck.remove(0);
    }

    public void addDiscarded(AmmoCard discardedCard) {
        discarded.add(discardedCard);
    }
}
