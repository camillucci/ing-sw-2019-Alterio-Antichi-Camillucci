package it.polimi.ingsw.view.gui;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;

public class Cache {
    private static HashMap<InputStream, Image> streamImages = new HashMap<>();
    private static HashMap<String, Image> stringImages = new HashMap<>();

    public static Image getImage (InputStream path) {
        for (InputStream key : streamImages.keySet())
            if (path == key)
                return streamImages.get(path);
        Image i = new Image(path);
        streamImages.put(path, i);
        return i;
    }

    public static Image getImage (String path) {
        for (String key : stringImages.keySet())
            if (path == key)
                return stringImages.get(path);
        Image i = new Image(path);
        stringImages.put(path, i);
        return i;
    }

    public static void initialize() {
        //todo add commonly used images
        return;
    }


}
