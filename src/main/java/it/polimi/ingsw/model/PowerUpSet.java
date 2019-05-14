package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class PowerUpSet
{
    private List<EndStartPowerUpCard> endStartPowerUpCards = new ArrayList<>();
    private List<InTurnPowerUpCard> inTurnPowerUpCards = new ArrayList<>();
    private List<CounterAttackPowerUpCard> counterAttackPowerUpCards = new ArrayList<>();

    public PowerUpSet(){}

    public PowerUpSet(PowerUpSet toCopy)
    {
        this.endStartPowerUpCards = new ArrayList<>(toCopy.getEndStartPUs());
        this.inTurnPowerUpCards = new ArrayList<>(toCopy.getInTurnPUs());
        this.counterAttackPowerUpCards = new ArrayList<>(toCopy.getCounterAttackPUs());
    }

    public void add(EndStartPowerUpCard powerUp)
    {
        this.endStartPowerUpCards.add(powerUp);
    }

    public void add(InTurnPowerUpCard powerUp)
    {
        this.inTurnPowerUpCards.add(powerUp);
    }

    public void add(CounterAttackPowerUpCard powerUp)
    {
        this.counterAttackPowerUpCards.add(powerUp);
    }

    public void add(PowerUpCard powerUp)
    {
        powerUp.addTo(this);
    }

    public void remove(EndStartPowerUpCard powerUp)
    {
        this.endStartPowerUpCards.remove(powerUp);
    }

    public void remove(InTurnPowerUpCard powerUp)
    {
        this.inTurnPowerUpCards.remove(powerUp);
    }

    public void remove(CounterAttackPowerUpCard powerUp)
    {
        this.counterAttackPowerUpCards.remove(powerUp);
    }

    public void remove(PowerUpCard powerUp)
    {
        powerUp.removeFrom(this);
    }

    public List<EndStartPowerUpCard> getEndStartPUs(){
        return new ArrayList<>(this.endStartPowerUpCards);
    }

    public List<InTurnPowerUpCard> getInTurnPUs(){
        return new ArrayList<>(this.inTurnPowerUpCards);
    }

    public List<CounterAttackPowerUpCard> getCounterAttackPUs()
    {
        return new ArrayList<>(this.counterAttackPowerUpCards);
    }

    public List<PowerUpCard> getAll()
    {
        List<PowerUpCard> ret = new ArrayList<>();
        ret.addAll(endStartPowerUpCards);
        ret.addAll(inTurnPowerUpCards);
        ret.addAll(counterAttackPowerUpCards);
        return ret;
    }
}
