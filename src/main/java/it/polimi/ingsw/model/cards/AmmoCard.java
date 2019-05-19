package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Ammo;

public class AmmoCard {

    public final Ammo ammo;
    public final boolean powerUpCard;

    public AmmoCard(Ammo ammo, boolean powerUpCard) {

        this.ammo = ammo;
        this.powerUpCard = powerUpCard;
    }

    public String getName() {
        String temp = "";
        if(ammo.blue > 0)
            temp = temp.concat(ammo.blue + "B");
        if(ammo.red > 0)
            if(temp.equals(""))
                temp = temp.concat(ammo.red + "R");
            else
                temp = temp.concat(ammo.red + " R");
        if(ammo.yellow > 0)
            if(temp.equals(""))
                temp = temp.concat(ammo.yellow + "Y");
            else
                temp = temp.concat(ammo.yellow + " Y");
        if(powerUpCard)
            temp = temp.concat(" PU");
        return temp;
    }
}
