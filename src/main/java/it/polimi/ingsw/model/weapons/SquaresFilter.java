package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.util.List;
import java.util.function.BiFunction;

public interface SquaresFilter extends BiFunction<Player, List<Square>, List<Square>>
{
    // kinda typedef
}
