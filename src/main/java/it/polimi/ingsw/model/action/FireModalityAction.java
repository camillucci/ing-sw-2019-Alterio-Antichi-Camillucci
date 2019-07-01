package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.Visualizable;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class FireModalityAction extends ExtendableAction
{
    public FireModalityAction(Ammo cost, String name, String description, List<Branch> branches)
    {
        this.doActionCost = cost;
        this.branches = new ArrayList<>(branches);
        this.visualizable = new Visualizable(nameToUrl(name,description), "use the " + description, name);
    }

    public FireModalityAction(Ammo cost, String name, String description, Branch ... branches)
    {
        this(cost, name, description, Arrays.asList(branches));
    }

    @Override
    public void addPowerUp(PowerUpCard powerUpCard)
    {
        if(getDiscardablePowerUps().contains(powerUpCard)) {
            this.discardedPowerUps.add(powerUpCard);
            Ammo powerUpAmmo = new Ammo(0, 0, 0);
            for(PowerUpCard pu : ownerPlayer.getPowerUps())
                powerUpAmmo = powerUpAmmo.add(pu.colorToAmmo());
            int i = 0;
            while(i < branches.size())
                if(branches.get(i).getCompatibleActions().get(0).doActionCost.isLessThan(powerUpAmmo))
                    branches.remove(branches.get(i));
                else
                    i++;
        }
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

    private String nameToUrl(String name, String description){
        return "/firemodality/" + name.concat("_").concat(description).replace(" ", "_").concat(".png").toLowerCase();
    }
}
