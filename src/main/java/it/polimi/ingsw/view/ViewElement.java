package it.polimi.ingsw.view;

import it.polimi.ingsw.model.snapshots.MatchSnapshot;

public abstract class ViewElement {
    public abstract void onNewMessage(String message);
    public abstract void disconnectedPlayerMessage(String name);
    public abstract void newPlayerMessage(String name);
    public abstract void timerStartedMessage(int time);
    public abstract void timerTickMessage(int time);
    public abstract void reconnectedMessage(String name);
    public abstract void winnerMessage(String winner);
    public abstract void scoreboardMessage(String[][] scoreboard);
    public abstract void onModelChanged(MatchSnapshot matchSnapshot);
}
