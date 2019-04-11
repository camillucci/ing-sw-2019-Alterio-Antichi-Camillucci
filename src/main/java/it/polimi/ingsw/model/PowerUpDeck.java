package it.polimi.ingsw.model;

import it.polimi.ingsw.model.powerups.*;

import static it.polimi.ingsw.model.AmmoColor.*;

import java.util.ArrayList;
import java.util.Collections;

public class PowerUpDeck {

    private ArrayList<PowerUpCard> deck = new ArrayList<>();
    private ArrayList<PowerUpCard> discarded = new ArrayList<>();
    private static final int COPY_OF_CARDS = 2;

    public PowerUpDeck() {
        for (int i = 0; i < COPY_OF_CARDS; i++) {
            deck.add(new TargetingScope(BLUE));
            deck.add(new Newton(BLUE));
            deck.add(new TagbackGrenade(BLUE));
            deck.add(new Teleporter(BLUE));
            deck.add(new TargetingScope(RED));
            deck.add(new Newton(RED));
            deck.add(new TagbackGrenade(RED));
            deck.add(new Teleporter(RED));
            deck.add(new TargetingScope(YELLOW));
            deck.add(new Newton(YELLOW));
            deck.add(new TagbackGrenade(YELLOW));
            deck.add(new Teleporter(YELLOW));
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
