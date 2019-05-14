package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.CardsFactory;
import it.polimi.ingsw.model.cards.PowerUpCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PowerUpDeck {

    private List<PowerUpCard> deck;
    private List<PowerUpCard> discarded = new ArrayList<>();

    public PowerUpDeck() {
        deck = CardsFactory.getPowerUps();
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
