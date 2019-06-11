package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * This class represent a set of ammo, it contains all three AmmoColors (Blue, Red, Yellow)
 * and all operations and evaluation that are needed to be done
 */
public class Ammo implements Serializable {

    public final int blue;
    public final int red;
    public final int yellow;

    /**
     * This constructor creates an immutable set of ammo
     * @param b The number of blue ammo
     * @param r The number of red ammo
     * @param y The number of yellow ammo
     */
    public Ammo(int b, int r, int y) {
        this.red = r;
        this.blue = b;
        this.yellow = y;
    }

    /**
     * This method returns a new ammo which is the difference from this and a given ammo
     * @param ammo The ammo we need to subtract
     * @return A new ammo which is the difference from this and a given ammo
     */
    public Ammo sub(Ammo ammo) {
        return new Ammo(this.blue - ammo.blue, this.red - ammo.red, this.yellow - ammo.yellow);
    }

    /**
     * This method returns a new ammo which is the sum from this and a given ammo
     * @param ammo The ammo we need to add
     * @return A new ammo which is the sum from this and the given ammo
     */
    public Ammo add(Ammo ammo) {
        return new Ammo(this.blue + ammo.blue, this.red + ammo.red, this.yellow + ammo.yellow);
    }

    /**
     * This method return true if this is less than a given Ammo, false otherwise
     * @param ammo The ammo we need to compare
     * @return True if this is less than the given Ammo, false otherwise
     */
    public boolean isLessThan(Ammo ammo) {
        return this.isLessOrEqualThan(ammo) && !this.isEqual(ammo);
    }

    /**
     * This method return true if this is less or equal than a given Ammo, false otherwise
     * @param ammo The ammo we need to compare
     * @return True if this is less or equal than the given Ammo, false otherwise
     */
    public boolean isLessOrEqualThan(Ammo ammo) {
        return this.blue <= ammo.blue && this.red <= ammo.red && this.yellow <= ammo.yellow;
    }

    /**
     * This method return true if this is greater or equal than a given Ammo, false otherwise
     * @param ammo The ammo we need to compare
     * @return True if this is greater or equal than the given Ammo, false otherwise
     */
    public boolean isGreaterOrEqual(Ammo ammo) {
        return ammo.isLessOrEqualThan(this);
    }

    /**
     * This method return true if this is greater than a given Ammo, false otherwise
     * @param ammo The ammo we need to compare
     * @return True if this is greater than the given Ammo, false otherwise
     */
    public boolean isGreaterThan(Ammo ammo) {
        return ammo.isLessThan(this);
    }

    /**
     * This method return true if this is equal to a given Ammo, false otherwise
     * @param ammo The ammo we need to compare
     * @return True if this is equal to the given Ammo, false otherwise
     */
    public boolean isEqual(Ammo ammo) {
        return this.blue == ammo.blue && this.red == ammo.red && this.yellow == ammo.yellow;
    }

    /**
     * This method returns the name of a single ammo, it is used for discarding ammo through the InTurnPowerUpCards
     * @return The name of a single ammo, one among Blue, Red, Yellow
     */
    public String getName() {
        if(blue == 1)
            return "Blue";
        if(red == 1)
            return "Red";
        return "Yellow";
    }
}
