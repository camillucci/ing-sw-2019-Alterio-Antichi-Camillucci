package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.CounterAttackPowerUpCard;
import it.polimi.ingsw.model.cards.EndStartPowerUpCard;
import it.polimi.ingsw.model.cards.InTurnPowerUpCard;
import it.polimi.ingsw.model.cards.PowerUpCard;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent the PowerUpCards a Player have,
 * every Player can have at most 3 PowerUpCard,
 * the PowerUpCards are divided in three groups: EndStart, InTurn, CounterAttack.
 */
public class PowerUpSet
{
    private List<EndStartPowerUpCard> endStartPowerUpCards = new ArrayList<>();
    private List<InTurnPowerUpCard> inTurnPowerUpCards = new ArrayList<>();
    private List<CounterAttackPowerUpCard> counterAttackPowerUpCards = new ArrayList<>();

    /**
     * This constructor creates an empty PowerUpSet, used when creating anew Player
     */
    public PowerUpSet(){}

    /**
     * This is a copy constructor, it creates a new PowerUpSet that is the copy of a given one
     * @param toCopy The PowerUpSet to copy
     */
    public PowerUpSet(PowerUpSet toCopy)
    {
        this.endStartPowerUpCards = new ArrayList<>(toCopy.getEndStartPUs());
        this.inTurnPowerUpCards = new ArrayList<>(toCopy.getInTurnPUs());
        this.counterAttackPowerUpCards = new ArrayList<>(toCopy.getCounterAttackPUs());
    }

    /**
     * This method adds an EndStartPowerUpCard to the corresponding list
     * @param powerUp The EndStartPowerUpCard to add
     */
    public void add(EndStartPowerUpCard powerUp)
    {
        this.endStartPowerUpCards.add(powerUp);
    }

    /**
     * This method adds an InTurnPowerUpCard to the corresponding list
     * @param powerUp The InTurnPowerUpCard to add
     */
    public void add(InTurnPowerUpCard powerUp)
    {
        this.inTurnPowerUpCards.add(powerUp);
    }

    /**
     * This method adds a CounterAttackPowerUpCard to the corresponding list
     * @param powerUp The CounterAttackPowerUpCard to add
     */
    public void add(CounterAttackPowerUpCard powerUp)
    {
        this.counterAttackPowerUpCards.add(powerUp);
    }

    /**
     * This method adds a generic PowerUpCard to the proper list
     * @param powerUp The PowerUpCard to add
     */
    public void add(PowerUpCard powerUp)
    {
        powerUp.addTo(this);
    }

    /**
     * This method removes an EndStartPowerUpCard from the corresponding list
     * @param powerUp The EndStartPowerUpCard to remove
     */
    public void remove(EndStartPowerUpCard powerUp)
    {
        this.endStartPowerUpCards.remove(powerUp);
    }

    /**
     * This method removes an InTurnPowerUpCard from the corresponding list
     * @param powerUp The InTurnPowerUpCard to remove
     */
    public void remove(InTurnPowerUpCard powerUp)
    {
        this.inTurnPowerUpCards.remove(powerUp);
    }

    /**
     * This method removes a CounterAttackPowerUpCard from the corresponding list
     * @param powerUp The CounterAttackPowerUpCard to remove
     */
    public void remove(CounterAttackPowerUpCard powerUp)
    {
        this.counterAttackPowerUpCards.remove(powerUp);
    }

    /**
     * This method removes a generic PowerUpCard from the corresponding list
     * @param powerUp The PowerUpCard to remove
     */
    public void remove(PowerUpCard powerUp)
    {
        powerUp.removeFrom(this);
    }

    /**
     * This method return the list of EndStartPowerUpCards this PowerUpSet contains
     * @return The list of EndStartPowerUpCards this PowerUpSet contains
     */
    public List<EndStartPowerUpCard> getEndStartPUs(){
        return new ArrayList<>(this.endStartPowerUpCards);
    }

    /**
     * This method return the list of InTurnPowerUpCard this PowerUpSet contains
     * @return The list of InTurnPowerUpCard this PowerUpSet contains
     */
    public List<InTurnPowerUpCard> getInTurnPUs(){
        return new ArrayList<>(this.inTurnPowerUpCards);
    }

    /**
     * This method return the list of CounterAttackPowerUpCard this PowerUpSet contains
     * @return The list of CounterAttackPowerUpCard this PowerUpSet contains
     */
    public List<CounterAttackPowerUpCard> getCounterAttackPUs()
    {
        return new ArrayList<>(this.counterAttackPowerUpCards);
    }

    /**
     * This method return a combine list of all PowerUpCards this PowerUpSet contains
     * @return The list of all PowerUpCards this PowerUpSet contains
     */
    public List<PowerUpCard> getAll()
    {
        List<PowerUpCard> ret = new ArrayList<>();
        ret.addAll(endStartPowerUpCards);
        ret.addAll(inTurnPowerUpCards);
        ret.addAll(counterAttackPowerUpCards);
        return ret;
    }
}
