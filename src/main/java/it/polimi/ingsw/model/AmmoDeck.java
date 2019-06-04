package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.AmmoCard;
import it.polimi.ingsw.model.cards.CardsFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represent the deck that contains all AmmoCards not currently in play
 */
public class AmmoDeck {

    /**
     * The list of AmmoCards not already drawn
     */
    private List<AmmoCard> deck;
    /**
     * The list of discarded AmmoCards
     */
    private List<AmmoCard> discarded = new ArrayList<>();

    /**
     * Create a new deck of AmmoCards and shuffles it
     */
    AmmoDeck() {
        deck = CardsFactory.getAmmo();
        Collections.shuffle(deck);
    }

    /**
     * This method returns an AmmoCard which is removed from the deck
     * @return An AmmoCard which is removed from the deck
     */
    public AmmoCard draw() {
        if(deck.isEmpty()) {
            deck = discarded;
            discarded = new ArrayList<>();
        }
        return deck.remove(0);
    }

    /**
     * This method adds a given AmmoCard to the discarded list
     * @param discardedCard The AmmoCard that need to be added to the discarded list
     */
    public void addDiscarded(AmmoCard discardedCard) {
        discarded.add(discardedCard);
    }
}
