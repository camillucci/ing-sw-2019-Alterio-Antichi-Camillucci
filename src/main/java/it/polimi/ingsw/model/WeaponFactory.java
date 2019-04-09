package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class WeaponFactory
{
    private static ArrayList<WeaponCard> weapons = new ArrayList<>();
    private static boolean weaponsCreated = false;

    public static List<WeaponCard> getWeapons()
    {
        return new ArrayList<WeaponCard>(weapons);
    }
}
