package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.generics.TriFunction;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.util.List;

public interface PlayersFilter extends TriFunction<Player, List<Player>, List<Square>, List<Player>>
{
    // kinda typedef
}
