package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.EndBranchAction;
import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.cards.AmmoCard;
import it.polimi.ingsw.model.cards.PowerUpCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AmmoSquare extends Square {

    private AmmoDeck ammoDeck;
    private AmmoCard ammoCard;

    public AmmoSquare(String name, int y, int x, SquareBorder[] borders, AmmoDeck ammoDeck) {
        super(name, y, x, borders);
        this.ammoDeck = ammoDeck;
        this.ammoCard = ammoDeck.draw();
        this.players = new ArrayList<>();
    }

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

    @Override
    public void refill() {
        if(ammoCard == null)
            ammoCard = ammoDeck.draw();
    }

    @Override
    public List<String> getCardsName() {
        return ammoCard != null ? Collections.singletonList(ammoCard.getName()) : Collections.emptyList();
    }
}
