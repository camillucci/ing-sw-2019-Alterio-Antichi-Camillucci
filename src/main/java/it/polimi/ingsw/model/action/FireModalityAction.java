package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class FireModalityAction extends ExtendableAction
{
    public FireModalityAction(Ammo cost, List<Branch> branches)
    {
        super(branches);
        this.doActionCost = cost;
    }

    public FireModalityAction(Ammo cost, Branch ... branches)
    {
        this(cost, Arrays.asList(branches));
    }

    @Override
    public List<PowerUpCard> getDiscardablePowerUps() {
        LinkedHashSet<PowerUpCard> temp = new LinkedHashSet<>();
        Ammo alreadyAdded = new Ammo(0, 0, 0);
        for(PowerUpCard pu : discardedPowerUps)
            alreadyAdded = alreadyAdded.add(pu.colorToAmmo());
        for(PowerUpCard pu : ownerPlayer.getPowerUps())
            if(!discardedPowerUps.contains(pu) && alreadyAdded.add(pu.colorToAmmo()).isLessOrEqualThan(this.doActionCost))
                temp.add(pu);
        return new ArrayList<>(temp);
    }
}
