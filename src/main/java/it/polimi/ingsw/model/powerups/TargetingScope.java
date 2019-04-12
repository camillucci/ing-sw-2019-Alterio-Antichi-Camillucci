package it.polimi.ingsw.model.powerups;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PowerUpCard;
import it.polimi.ingsw.model.Square;

import java.util.List;

public class TargetingScope extends PowerUpCard {

    public TargetingScope(AmmoColor color)
    {
        super("TargetingScope", null, color); //TODO
    }
    @Override
    public List<Player> VisiblePlayers(Player player, List<Player> alreadyAdded) {
        return null;
    }

    @Override
    public List<Square> visibleSquares(Player player) {
        return null;
    }
}