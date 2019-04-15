package it.polimi.ingsw.model;

import it.polimi.ingsw.model.powerups.*;

import java.util.ArrayList;
import java.util.Collections;

public class PowerUpDeck {

    private ArrayList<PowerUpCard> deck = new ArrayList<>();
    private ArrayList<PowerUpCard> discarded = new ArrayList<>();
    private static final int COPY_OF_CARDS = 2;

    public PowerUpDeck() {
        for (int i = 0; i < COPY_OF_CARDS; i++)
            for(AmmoColor color : AmmoColor.values()){
            deck.add(new TargetingScope(color));
            deck.add(new Newton(color));
            deck.add(new TagbackGrenade(color));
            deck.add(new Teleporter(color));
        }
        Collections.shuffle(deck);
    }

    public PowerUpCard draw() {
        if(deck.isEmpty()) {
            deck = discarded;
            discarded = new ArrayList<>();
        }
        return deck.remove(0);
    }

    public void addDiscarded(PowerUpCard discardedCard) {
        discarded.add(discardedCard);
    }
}
