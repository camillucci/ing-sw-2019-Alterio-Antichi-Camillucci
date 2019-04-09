package it.polimi.ingsw.model;
import java.util.ArrayList;
import java.util.Random;

public class WeaponDeck {

    private ArrayList<WeaponCard> deck = new ArrayList<>();
    private Random rand = new Random();

    public WeaponDeck() {
        //TODO Add all cards
    }

    public WeaponCard draw() {
        if(deck.isEmpty()) {
            return null;
        }
        return deck.remove(rand.nextInt(deck.size()));
    }
}
