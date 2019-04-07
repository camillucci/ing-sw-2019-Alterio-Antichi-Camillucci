package it.polimi.ingsw.model;

import it.polimi.ingsw.model.powerups.*;

import static it.polimi.ingsw.model.AmmoColor.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public class PowerUpDeck {

    private ArrayList<PowerUpCard> deck = new ArrayList<>();
    private ArrayList<PowerUpCard> discarded = new ArrayList<>();
    private Random rand = new Random();

    public PowerUpDeck() {
        IntStream.range(0, 2).forEach(i -> {
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
        });
    }

    public PowerUpCard draw() {
        if(this.isEmpty()) {
            deck = discarded;
        }
        return deck.remove(rand.nextInt(deck.size()));
    }

    public void addDiscarded(PowerUpCard discardedCard) {
        discarded.add(discardedCard);
    }

    private boolean isEmpty() { return deck.isEmpty(); }
}
