package it.polimi.ingsw.model;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.PowerUpAction;
import it.polimi.ingsw.model.branch.*;

import java.util.ArrayList;
import java.util.List;

public class Turn implements ActionsProvider {

    public final Event<Turn, Player> endTurnEvent = new Event<>();
    public final Event<Turn, List<Action>> newActionsEvent = new Event<>();
    private static int frenzyCounter = 0;
    private Player currentPlayer;
    private int moveCounter = 2;
    private BranchMap branchMap;
    private Match match;
    private List<Player> clonedPlayers;
    private List<Player> clonedDeadPlayers;

    public Turn(Player currentPlayer, Match match) {
        this.currentPlayer = currentPlayer;
        this.match = match;
        newMove();
    }

    private void newMove()
    {
        createBranchMap();
    }

    private void standardEventsSetup() {
        this.branchMap.rollbackEvent.addEventHandler((s, e) -> rollback());
        this.branchMap.newActionsEvent.addEventHandler((s, actions) -> {
            actions.forEach(a -> a.initialize(currentPlayer));
            this.newActionsEvent.invoke(this, actions);
        });
    }

    private void onMoveTerminated() {
        moveCounter--;
        if (moveCounter == -1)
            endTurnEvent.invoke(this, currentPlayer);
        else
        {
            newMove();
            this.newActionsEvent.invoke(this, getActions());
        }
    }

    private void createBranchMap() {
        clonePlayers();

        if(moveCounter == 0)
            this.branchMap = BranchMapFactory.reloadEndTurn();
        else if (match.getFinalFrenzy()) {
            if (frenzyCounter <= match.getPlayers().size() - match.getFrenzyStarter())
                this.branchMap = BranchMapFactory.adrenalineX2();
            else
                this.branchMap = BranchMapFactory.adrenalineX1();
            increaseFrenzyCounter();
        }
        else if (currentPlayer.getDamage().size() >= 3)
            if (currentPlayer.getDamage().size() >= 6)
                this.branchMap = BranchMapFactory.sixDamage();
            else
                this.branchMap = BranchMapFactory.threeDamage();
        else
            this.branchMap = BranchMapFactory.noAdrenaline();

        standardEventsSetup();
        this.branchMap.endOfBranchMapReachedEvent.addEventHandler((a, b) -> onMoveTerminated());
    }

    private void rollback() {
        match.rollback(clonedPlayers, clonedDeadPlayers);
        createBranchMap();
    }

    private void clonePlayers() {
        clonedPlayers = new ArrayList<>();
        clonedDeadPlayers = new ArrayList<>();
        List<Player> deadPlayers = match.getDeadPlayers();
        for (Player p : match.getPlayers()) {
            Player clone = p.getClone();
            clonedPlayers.add(clone);
            if (deadPlayers.contains(p))
                clonedDeadPlayers.add(p);
        }
    }

    private static void increaseFrenzyCounter() {
        frenzyCounter++;
    }

    @Override
    public Player getPlayer() {
        return currentPlayer;
    }

    @Override
    public List<Action> getActions() {
        List<Action> ret = this.branchMap.getPossibleActions();
        for(Action a : ret)
            a.initialize(currentPlayer);
        return ret;
    }
}
