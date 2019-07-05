package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.generics.Visualizable;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class used to create the actions that represent the effects of the various weapons present in the game
 */
public class FireModalityAction extends ExtendableAction
{
    /**
     * Constructor that assigns the input parameters to their correspondences
     * @param cost Amount of ammo needed to execute the action
     * @param name String that represents the action
     * @param description String used to further explain the effect of the action (used for visual purposes)
     * @param branches List of branches that can be entered by executing the action.
     */
    public FireModalityAction(Ammo cost, String name, String description, List<Branch> branches)
    {
        this.doActionCost = cost;
        this.branches = new ArrayList<>(branches);
        this.visualizable = new Visualizable(nameToUrl(name,description), "use the " + description, name);
    }

    /**
     * Alternative constructor
     * @param cost Amount of ammo needed to execute the action
     * @param name String that represents the action
     * @param description String used to further explain the effect of the action (used for visual purposes)
     * @param branches List of branches that can be entered by executing the action.
     */
    public FireModalityAction(Ammo cost, String name, String description, Branch ... branches)
    {
        this(cost, name, description, Arrays.asList(branches));
    }

    /**
     * Method that converts the name associated with the action to an image
     * @param name String that represents the action
     * @param description String used to further explain the effect of the action (used for visual purposes)
     * @return The path used to get the image when the action is going to be displayed to the user
     */
    private String nameToUrl(String name, String description){
        return "/firemodality/" + name.concat("_").concat(description).replace(" ", "_").concat(".png").toLowerCase();
    }

    public Ammo getCost() {
        return new Ammo(doActionCost);
    }
}
