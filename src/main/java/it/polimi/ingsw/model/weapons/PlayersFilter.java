package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.Player;

import java.util.List;
import java.util.function.BiFunction;

public interface PlayersFilter extends BiFunction<Player, List<Player>, List<Player>>
{
    // kinda typedef
}
