package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.generics.TriConsumer;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.util.List;

public interface ShootFunc extends TriConsumer<Player, List<Player>, List<Square>>
{
    default ShootFunc andThen(ShootFunc after)
    {
        return (a,b,c) -> {accept(a,b,c); after.accept(a,b,c);};
    }
}
