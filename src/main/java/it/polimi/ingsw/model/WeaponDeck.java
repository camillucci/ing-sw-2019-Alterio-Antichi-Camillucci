package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.CardsFactory;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.Collections;
import java.util.List;

/**
 * This class represent the deck that contains all WeaponCards not currently in play
 */
public class WeaponDeck {

    /**
     * The list of WeaponCards not already drawn
     */
    private List<WeaponCard> deck;

    /**
     * Create a new deck of WeaponCards and shuffles it
     */
    public WeaponDeck() {
        deck = CardsFactory.getWeapons();
        Collections.shuffle(deck);
    }

    /**
     * This method returns a WeaponCard which is removed from the deck
     * @return A WeaponCard which is removed from the deck
     */
    public WeaponCard draw() {
        if(deck.isEmpty())
            return null;
        return deck.remove(0);
    }
}
