package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.EndBranchAction;
import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.cards.AmmoCard;
import it.polimi.ingsw.model.cards.PowerUpCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represent one of the squares that contains AmmoCard which can be grabbed
 */
public class AmmoSquare extends Square {

    /**
     * The deck with all remaining and discarded AmmoCards
     * It is used to refill the square at the end of the turn and to addTarget discarded AmmoCards
     */
    private AmmoDeck ammoDeck;
    /**
     * The current AmmoCard on the Square
     */
    private AmmoCard ammoCard;

    /**
     * Create a new AmmoSquare as a part of the GameBoard, it also draw the first AmmoCard
     * @param name The name of the square, from 'A' to 'I'
     * @param y The first coordinate
     * @param x The second coordinate
     * @param borders The 4 SquareBorder, one for each cardinal direction
     * @param ammoDeck The AmmoDeck for refill and to addTarget discarded AmmoCards
     */
    public AmmoSquare(String name, int y, int x, SquareBorder[] borders, AmmoDeck ammoDeck) {
        super(name, y, x, borders);
        this.ammoDeck = ammoDeck;
        this.ammoCard = ammoDeck.draw();
        this.players = new ArrayList<>();
    }

    /**
     * This method give to a given Player the resources written on the AmmoCard if available,
     * it is used through a GrabAction
     * @param player The Player in which addTarget the resources (Ammo and PowerUpCard)
     * @param powerUpCards It's always an empty list and not used hero, only for SpawnAndShopSquare
     * @return A single Branch from which the GrabAction can be confirmed by the Player
     */
    @Override
    public List<Branch> grab(Player player, List<PowerUpCard> powerUpCards) {
        if(!this.isEmpty()) {
            player.addBlue(ammoCard.ammo.blue);
            player.addRed(ammoCard.ammo.red);
            player.addYellow(ammoCard.ammo.yellow);

            if (ammoCard.powerUpCard) {
                player.addPowerUpCard();
            }
            ammoDeck.addDiscarded(ammoCard);
            ammoCard = null;
        }
        return Collections.singletonList(new Branch(Collections.emptyList(), new EndBranchAction()));
    }

    public boolean isEmpty() {
        return ammoCard == null;
    }

    /**
     * This method addTarget a new AmmoCard to the AmmoSquare at the end of the Turn if it was grabbed this turn
     */
    @Override
    public void refill() {
        if(ammoCard == null)
            ammoCard = ammoDeck.draw();
    }

    /**
     * This method return the name of the AmmoCard if present
     * @return A list containing the name of the AmmoCard if present, an empty list otherwise
     */
    @Override
    public List<String> getCardsName() {
        return ammoCard != null ? Collections.singletonList(ammoCard.getName()) : Collections.emptyList();
    }
}
