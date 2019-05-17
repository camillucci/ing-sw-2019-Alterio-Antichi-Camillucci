package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class GrabAction extends ExtendableAction
{
    @Override
    protected void op()
    {
        this.grab();
    }

    private void grab()
    {
        this.branches = this.ownerPlayer.getCurrentSquare().grab(this.ownerPlayer, this.discardedPowerUps);
    }

    @Override
    public List<PowerUpCard> getDiscardablePowerUps() {
        LinkedHashSet<PowerUpCard> temp = new LinkedHashSet<>();
        Ammo alreadyAdded = new Ammo(0, 0, 0);
        for(PowerUpCard pu : discardedPowerUps)
            alreadyAdded = alreadyAdded.add(pu.colorToAmmo());
        for(WeaponCard wc : ownerPlayer.getCurrentSquare().getWeapons())
            for(PowerUpCard pu : ownerPlayer.getPowerUps())
                if(!discardedPowerUps.contains(pu) && alreadyAdded.add(pu.colorToAmmo()).isLessOrEqualThan(wc.buyCost))
                    temp.add(pu);
        return new ArrayList<>(temp);
    }
}
