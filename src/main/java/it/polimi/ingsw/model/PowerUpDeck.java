package it.polimi.ingsw.model;

import it.polimi.ingsw.model.powerups.*;

import static it.polimi.ingsw.model.AmmoColor.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public class PowerUpDeck {

    private int numberOfCards;
    private ArrayList<PowerUpCard> deck = new ArrayList<>();
    private ArrayList<PowerUpCard> discarded = new ArrayList<>();
    private Random rand = new Random();

    public PowerUpDeck() {
        IntStream.range(0, 2).forEach(i -> {
            deck.add(new PowerUpTargetingScope(BLUE));
            deck.add(new PowerUpNewton(BLUE));
            deck.add(new PowerUpTagbackGrenade(BLUE));
            deck.add(new PowerUpTeleporter(BLUE));
            deck.add(new PowerUpTargetingScope(RED));
            deck.add(new PowerUpNewton(RED));
            deck.add(new PowerUpTagbackGrenade(RED));
            deck.add(new PowerUpTeleporter(RED));
            deck.add(new PowerUpTargetingScope(YELLOW));
            deck.add(new PowerUpNewton(YELLOW));
            deck.add(new PowerUpTagbackGrenade(YELLOW));
            deck.add(new PowerUpTeleporter(YELLOW));
        });
        this.numberOfCards = deck.size();
    }

    public PowerUpCard draw() {
        if(this.isEmpty()) {
            deck = discarded;
            numberOfCards = deck.size();
        }
        numberOfCards--;
        return deck.remove(rand.nextInt(deck.size()));
    }

    public boolean isEmpty() { return deck.isEmpty(); }
}
