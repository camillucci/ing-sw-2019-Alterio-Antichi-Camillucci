package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.generics.TriFunction;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.util.List;

public interface SquaresFilter extends TriFunction<Player, List<Player>, List<Square>, List<Square>>
{
    // Typedef of how a Square can be targeted
}
