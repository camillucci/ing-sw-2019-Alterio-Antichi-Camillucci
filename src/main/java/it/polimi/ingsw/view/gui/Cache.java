package it.polimi.ingsw.view.gui;
import javafx.scene.image.Image;

import java.util.HashMap;

public class Cache {
    HashMap<String, Image> images = new HashMap<>();

    public Cache newCache () {
        initialize();
        Cache cache = new Cache();
        return cache;
    }

    public Image getImage (String path) {
        for (String key : images.keySet())
            if (path == key)
                return images.get(path);
        Image i = new Image(path);
        images.put(path, i);
        return i;
    }

    private void initialize() {
        //todo add commonly used images
        return;
    }


}
