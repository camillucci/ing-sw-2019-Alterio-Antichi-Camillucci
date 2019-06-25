package it.polimi.ingsw.view.gui.actionhandler;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Avatar extends ImageView
{
    private String color;
    private String name;
    public Avatar(){}
    public Avatar(String color){
        this.color = color;
        setImage();
    }

    private void setImage(){
        setImage(new Image("player/" + color.toLowerCase() + "_avatar.png"));
    }

    public String getColor(){
        return color;
    }
    public void setColor(String color){
        this.color = color;
        setImage();
    }
}
