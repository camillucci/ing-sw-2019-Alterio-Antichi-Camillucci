package it.polimi.ingsw.view.gui.actionhandler;

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
        setImage(new Image(this.color + "_ammo.png"));
    }
}