package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.view.gui.Cache;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Avatar extends ImageView
{
    private String color = null;
    private String name;
    public Avatar(){}
    public Avatar(String color){
        this.color = color;
        setImage();
    }

    private void setImage(){
        String url = "/player/" + color.toLowerCase() + "_avatar.png";
        setImage(Cache.getImage(getClass().getResourceAsStream(url)));
    }

    public String getColor(){
        return color;
    }
    public void setColor(String color){
        this.color = color;
        setImage();
    }
}
