package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class PowerupSet implements Cloneable
{
    private List<EndStartPowerUpCard> endStartPowerupCards = new ArrayList<>();
    private List<InTurnPowerUpCard> inTurnPowerupCards = new ArrayList<>();
    private List<CounterAttackPowerUpCard> counterAttackPowerupCards = new ArrayList<>();

    public PowerupSet(){}
    public PowerupSet(PowerupSet toCopy)
    {
        this.endStartPowerupCards = new ArrayList<>(toCopy.getEndStartPUs());
        this.inTurnPowerupCards = new ArrayList<>(toCopy.getInTurnPUs());
        this.counterAttackPowerupCards = new ArrayList<>(toCopy.getCounterAttackPUs());
    }
    public void add(EndStartPowerUpCard powerup)
    {
        this.endStartPowerupCards.add(powerup);
    }

    public void add(InTurnPowerUpCard powerup)
    {
        this.inTurnPowerupCards.add(powerup);
    }

    public void add(CounterAttackPowerUpCard powerup)
    {
        this.counterAttackPowerupCards.add(powerup);
    }

    public void add(PowerUpCard powerup)
    {
        powerup.addTo(this);
    }

    public void remove(EndStartPowerUpCard powerup)
    {
        this.endStartPowerupCards.remove(powerup);
    }

    public void remove(InTurnPowerUpCard powerup)
    {
        this.inTurnPowerupCards.remove(powerup);
    }

    public void remove(CounterAttackPowerUpCard powerup)
    {
        this.counterAttackPowerupCards.remove(powerup);
    }

    public void remove(PowerUpCard powerup)
    {
        powerup.removeFrom(this);
    }

    public List<EndStartPowerUpCard> getEndStartPUs(){
        return new ArrayList<>(this.endStartPowerupCards);
    }

    public List<InTurnPowerUpCard> getInTurnPUs(){
        return new ArrayList<>(this.inTurnPowerupCards);
    }

    public List<CounterAttackPowerUpCard> getCounterAttackPUs()
    {
        return new ArrayList<>(this.counterAttackPowerupCards);
    }

    public List<PowerUpCard> getAll()
    {
        List<PowerUpCard> ret = new ArrayList<>();
        ret.addAll(endStartPowerupCards);
        ret.addAll(inTurnPowerupCards);
        ret.addAll(counterAttackPowerupCards);
        return ret;
    }

    @Override
    public PowerupSet clone()
    {
        try{
            return (PowerupSet)super.clone();
        }
        catch(Exception e) {}//todo
        return null;
    }
}
