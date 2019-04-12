package it.polimi.ingsw.model.powerups;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PowerUpCard;
import it.polimi.ingsw.model.Square;

import java.util.List;

public class Newton extends PowerUpCard {

    public Newton(AmmoColor color) {
        super("Newton", null, color); //TODO cost
    }

    @Override
    public List<Player> VisiblePlayers(Player player, List<Player> alreadyAdded) {
        //TODO;
        return  null;
    }

    @Override
    public List<Square> visibleSquares(Player player) {
        //TODO;
        return null;
    }
}
