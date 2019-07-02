package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.view.gui.Cache;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AmmoImageView extends ImageView
{
    private String color;

    public String getColor(){
        return color;
    }
    public void setColor(String color){
        this.color = color.toLowerCase();
        String url = "/" + this.color.toLowerCase() + "_ammo.png";
        setImage(Cache.getImage(getClass().getResourceAsStream(url)));
    }
}