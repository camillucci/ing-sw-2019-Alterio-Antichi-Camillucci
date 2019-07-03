package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.view.gui.Animations;
import it.polimi.ingsw.view.gui.Cache;
import javafx.animation.RotateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Avatar extends ImageView
{
    private String color = null;
    private String name;
    private RotateTransition rotateTransition;
    public Avatar(){}
    public Avatar(String color){
        this.color = color;
        setImage();
    }

    private void setImage(){
        String url = "/player/" + color.toLowerCase() + "_avatar.png";
        setImage(Cache.getImage(url));
    }

    public void disconnected(){
        rotateTransition = Animations.rotatingAnimation(this);
        rotateTransition.play();
    }

    public void reconnected(){
        if(rotateTransition != null)
        {
            rotateTransition.stop();
            this.setRotate(0);
            rotateTransition = null;
        }
    }

    public String getColor(){
        return color;
    }
    public void setColor(String color){
        this.color = color;
        setImage();
    }
}
