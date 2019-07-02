package it.polimi.ingsw.view.gui;
import it.polimi.ingsw.view.gui.actionhandler.PlayerCardsController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.util.HashMap;

public class Cache {
    private static HashMap<String, Image> stringImages = new HashMap<>();
    private static HashMap<Image, String> imagesString = new HashMap<>();
    private Cache(){}

    static {
        initialize();
    }

    public static synchronized Image getImage (String path) {
        Image ret = stringImages.get(path);
        if(ret == null)
            ret = loadImage(path);
        return ret;
    }

    private static void initialize() {
        loadMaps();
    }

    private static Image loadImage(String path){
        InputStream stream = PlayerCardsController.class.getResourceAsStream(path);
        Image image = new Image(stream);
        stringImages.put(path, image);
        imagesString.put(image, path);
        return image;
    }

    public static synchronized String imageToUrl(Image image){
        String ret = imagesString.get(image);
        if(ret != null)
            return ret;
        throw new RuntimeException("Image does not exist");
    }

    private static void loadMaps() {
        int totMaps = 3;
        for(int i = 0; i < totMaps; i++)
            loadImage("/map" + (i + 1) + ".png");
    }
}
