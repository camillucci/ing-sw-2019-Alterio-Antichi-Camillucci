package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.EndBranchAction;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AmmoSquare extends Square {

    private AmmoDeck ammoDeck;
    private AmmoCard ammoCard;

    public AmmoSquare(int y, int x, SquareBorder[] borders, AmmoDeck ammoDeck) {
        this.y = y;
        this.x = x;
        this.north = borders[0];
        this.south = borders[1];
        this.west = borders[2];
        this.east = borders[3];
        this.ammoDeck = ammoDeck;
        this.ammoCard = ammoDeck.draw();
        this.players = new ArrayList<>();
    }

    @Override
    public List<Branch> grab(Player player) {
        if(!this.isEmpty()) {
            player.addRed(ammoCard.getRed());
            player.addBlue(ammoCard.getBlue());
            player.addYellow(ammoCard.getYellow());

            if (ammoCard.isPowerUpCard()) {
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
