package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;

public interface MatchSnapshotProvider
{
    MatchSnapshot getMatchSnapshot();
    IEvent<MatchSnapshotProvider, MatchSnapshot> modelChangedEvent();
}
