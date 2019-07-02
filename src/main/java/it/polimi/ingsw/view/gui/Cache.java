package it.polimi.ingsw.view.gui;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.util.HashMap;

public class Cache {
    private static HashMap<String, Image> stringImages = new HashMap<>();

    static {
        initialize();
    }

    public static Image getImage (String path) {
        for (String key : stringImages.keySet())
            if (path.equals(key))
                return stringImages.get(path);
        return loadImage(path);
    }

    public static void initialize() {
        loadMaps();
    }

    private static Image loadImage(String path){
        InputStream stream = Cache.class.getResourceAsStream(path);
        Image image = new Image(stream);
        stringImages.put(path, image);
        return image;
    }

    private static void loadMaps() {
        int totMaps = 3;
        for(int i=0; i < totMaps; i++)
            loadImage("/map" + i + ".png");
    }
}
