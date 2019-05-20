package it.polimi.ingsw.view;

import it.polimi.ingsw.model.snapshots.MatchSnapshot;

public abstract class ViewElement {
    public abstract void onNewMessage(String message);
    public abstract void onModelChanged(MatchSnapshot matchSnapshot);
}
