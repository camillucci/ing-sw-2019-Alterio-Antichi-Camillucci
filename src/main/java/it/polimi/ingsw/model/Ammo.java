package it.polimi.ingsw.model;

public class Ammo {

    public final int blue;
    public final int red;
    public final int yellow;

    public Ammo(int b, int r, int y) {
        this.red = r;
        this.blue = b;
        this.yellow = y;
    }

    public Ammo sub(Ammo ammo) {
        return new Ammo(this.blue - ammo.blue, this.red - ammo.red, this.yellow - ammo.yellow);
    }

    public Ammo add(Ammo ammo) {
        return new Ammo(this.blue + ammo.blue, this.red + ammo.red, this.yellow + ammo.yellow);
    }

    public boolean isLessThan(Ammo ammo) {
        return this.isLessOrEqualThan(ammo) && !this.isEqual(ammo);
    }

    public boolean isLessOrEqualThan(Ammo ammo) {
        return this.blue <= ammo.blue && this.red <= ammo.red && this.yellow <= ammo.yellow;
    }

    public boolean isGreaterOrEqual(Ammo ammo) {
        return ammo.isLessOrEqualThan(this);
    }

    public boolean isGreaterThan(Ammo ammo) {
        return ammo.isLessThan(this);
    }

    public static Ammo getAmmo(Player player) {
        return new Ammo(player.getBlueAmmo(), player.getRedAmmo(), player.getYellowAmmo());
    }

    public boolean isEqual(Ammo ammo) {
        return this.blue == ammo.blue && this.red == ammo.red && this.yellow == ammo.yellow;
    }
}
