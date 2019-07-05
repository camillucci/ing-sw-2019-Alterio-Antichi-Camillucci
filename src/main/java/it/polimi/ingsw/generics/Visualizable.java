package it.polimi.ingsw.generics;

import java.io.Serializable;

/**
 * Utility class used to convert generic elements into images. It implements serializable because it's purpose is to
 * support the objects sent from the server to the client (that are eventually going to be displayed to the user).
 */
public class Visualizable implements Serializable {
    public final String imgPath;
    public final String icon;
    public final String description;
    public final String name;

    /**
     * Constructor that assigns every input parameter to their correspondences
     * @param imgPath Path required to get the image used to represent the object
     * @param iconPath Path to the icon of the object
     * @param description Textual description of the object, generically used to add information about the object that
     *                    can't be included in the name of image.
     * @param name Name of the represented object
     */
    public Visualizable(String imgPath, String iconPath, String description, String name)
    {
        this.imgPath = imgPath;
        this.icon = iconPath;
        this.description = description;
        this.name = name;
    }

    /**
     * Alternative constructor that only requires the name, the description and the image path of the object to be
     * specified.
     * @param imgPath Path required to get the image used to represent the object
     * @param description Textual description of the object, generically used to add information about the object that
     *                    can't be included in the name of image.
     * @param name Name of the represented object
     */
    public Visualizable(String imgPath, String description, String name){
        this(imgPath, null, description, name);
    }

    /**
     * Alternative constructor that only requires the name and the description of the object to be specified.
     * @param description Textual description of the object, generically used to add information about the object that
     *                    can't be included in the name of image.
     * @param name Name of the represented object
     */
    public Visualizable(String description, String name){
        this(null, description, name);
    }

    /**
     * Alternative constructor that only requires the name of the object to be specified.
     * @param name Name of the represented object
     */
    public Visualizable(String name){
        this("", name);
    }
}
