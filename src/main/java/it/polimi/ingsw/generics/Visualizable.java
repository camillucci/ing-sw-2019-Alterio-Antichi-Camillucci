package it.polimi.ingsw.generics;

import java.io.Serializable;

public class Visualizable implements Serializable {
    public final String imgPath;
    public final String icon;
    public final String description;
    public final String name;

    public Visualizable(String imgPath, String iconPath, String description, String name)
    {
        this.imgPath = imgPath;
        this.icon = iconPath;
        this.description = description;
        this.name = name;
    }

    public Visualizable(String imgPath, String description, String name){
        this(imgPath, null, description, name);
    }

    public Visualizable(String description, String name){
        this(null, description, name);
    }

    public Visualizable(String name){
        this("", name);
    }
}
