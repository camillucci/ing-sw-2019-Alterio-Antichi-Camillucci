package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Visualizable;

public class EndBranchAction extends Action {

    public EndBranchAction() {
        this.visualizable = new Visualizable(null, "next.png","end the move", "end");
    }
}
