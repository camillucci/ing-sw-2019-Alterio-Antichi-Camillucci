package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Ammo;

/**
 * This class includes all the info relative to specific ammo cards present in the game. Ammo cards can either be
 * strictly ammo related cards or they can be power up cards (which have the ability to produce ammo by being discarded)
 */
public class AmmoCard {

    /**
     * The amount of ammo (of each color) associated with the card
     */
    public final Ammo ammo;

    /**
     * Boolean that represents whether the card is a power up card or not (true if it is, false otherwise).
     */
    public final boolean powerUpCard;

    /**
     * Constructor. It assigns the 2 input parameters to their global correspondences
     * @param ammo The amount of ammo (of each color) associated with the card
     * @param powerUpCard Boolean that represents whether the card is a power up card or not (true if it is, false otherwise).
     */
    public AmmoCard(Ammo ammo, boolean powerUpCard) {

        this.ammo = ammo;
        this.powerUpCard = powerUpCard;
    }

    /**
     * Generates the name associated with the card by counting how much ammo of each color the card has. The final
     * check verifies whether it is a power up card.
     * @return The name associated to the card.
     */
    public String getName() {
        String temp = "";
        if(ammo.blue > 0)
            temp = temp.concat(ammo.blue + "B");
        if(ammo.red > 0)
            if(temp.equals(""))
                temp = temp.concat(ammo.red + "R");
            else
                temp = temp.concat(" " + ammo.red + "R");
        if(ammo.yellow > 0)
            if(temp.equals(""))
                temp = temp.concat(ammo.yellow + "Y");
            else
                temp = temp.concat(" " + ammo.yellow + "Y");
        if(powerUpCard)
            temp = temp.concat(" PU");
        return temp;
    }
}
