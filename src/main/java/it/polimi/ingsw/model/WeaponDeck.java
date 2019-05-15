package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.CardsFactory;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.Collections;
import java.util.List;

public class WeaponDeck {

    private List<WeaponCard> deck;

    public WeaponDeck() {
        deck = CardsFactory.getWeapons();
        Collections.shuffle(deck);
    }

    public WeaponCard draw() {
        if(deck.isEmpty())
            return null;
        return deck.remove(0);
    }
}
