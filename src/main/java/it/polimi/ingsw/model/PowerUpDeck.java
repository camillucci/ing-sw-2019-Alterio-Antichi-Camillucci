package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.CardsFactory;
import it.polimi.ingsw.model.cards.PowerUpCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represent the deck that contains all PowerUpCards not currently in play
 */
public class PowerUpDeck {

    /**
     * The list of PowerUpCards not already drawn
     */
    private List<PowerUpCard> deck;
    /**
     * The list of discarded PowerUpCards
     */
    private List<PowerUpCard> discarded = new ArrayList<>();

    /**
     * Create a new deck of PowerUpCards and shuffles it
     */
    public PowerUpDeck() {
        deck = CardsFactory.getPowerUps();
        Collections.shuffle(deck);
    }

    /**
     * This constructor is a copy constructor, it create a new PowerUpDeck that is the copy of a given one
     * @param powerUpDeck The PowerUpDeck that has to be copied
     */
    public PowerUpDeck(PowerUpDeck powerUpDeck) {
        this.deck = new ArrayList<>(powerUpDeck.deck);
        this.discarded = new ArrayList<>(powerUpDeck.discarded);
    }

    /**
     * This method returns a PowerUpCard which is removed from the deck
     * @return A PowerUpCard which is removed from the deck
     */
    public PowerUpCard draw() {
        if(deck.isEmpty()) {
            deck = discarded;
            discarded = new ArrayList<>();
        }
        return deck.remove(0);
    }

    /**
     * This method adds a given PowerUpCard to the discarded list
     * @param discardedCard The PowerUpCard that need to be added to the discarded list
     */
    public void addDiscarded(PowerUpCard discardedCard) {
        discarded.add(discardedCard);
    }
}
