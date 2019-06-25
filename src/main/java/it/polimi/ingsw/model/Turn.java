package it.polimi.ingsw.model;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.branch.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent a single turn of the Match,
 * it manage the actions a Player can do through the BranchMaps
 */
public class Turn extends ActionsProvider {

    /**
     * The event the Match listen, it's invoked when the turn is finished
     */
    public final IEvent<Turn, Player> endTurnEvent = new Event<>();
    /**
     * The counter used for counting how many Turns have passed since the activation of the final frenzy
     */
    private static int frenzyCounter = 0;
    /**
     * The Player currently playing the Turn
     */
    private Player currentPlayer;
    /**
     * The number of moves the currentPlayer has left to do, it starts at 2
     */
    private int moveCounter = 2;
    /**
     * The BranchMap from which a Player can choose the Action he can do during the turn
     */
    private BranchMap branchMap;
    /**
     * The reference to the Match of this Turn
     */
    private Match match;
    /**
     * The list of Players cloned at the beginning of each move, it's used for the rollback
     */
    private List<Player> clonedPlayers;
    /**
     * The list of dead Player cloned at the beginning of each move, it's used for the rollback
     */
    private List<Player> clonedDeadPlayers;

    /**
     * This constructor initialized the Turn with the Player and the Action it can do
     * @param currentPlayer The Player that will play this Turn
     * @param match The Match of this Turn
     */
    public Turn(Player currentPlayer, Match match) {
        this.currentPlayer = currentPlayer;
        this.match = match;
        newMove();
    }

    /**
     * This method create the BranchMap for a new move
     */
    private void newMove()
    {
        createBranchMap();
    }

    /**
     * This method initialized the events of the current BranchMap
     */
    private void standardEventsSetup() {
        this.branchMap.rollbackEvent.addEventHandler((s, e) -> rollback());
        this.branchMap.newActionsEvent.addEventHandler((s, actions) -> {
            actions.forEach(a -> a.initialize(currentPlayer));
            ((Event<Player, List<Action>>)this.newActionsEvent).invoke(currentPlayer, actions);
        });
    }

    /**
     * This method is invoked at the end of each move, it decrease the move counter and initialize a new BranchMap
     */
    private void onMoveTerminated() {
        moveCounter--;
        if (moveCounter == -1) {
            for(Square square : match.gameBoard.getSquares())
                square.refill();
            ((Event<Turn, Player>)endTurnEvent).invoke(this, currentPlayer);
        }
        else
        {
            newMove();
            ((Event<Player, List<Action>>)this.newActionsEvent).invoke(currentPlayer, getActions());
        }
    }

    /**
     * This method creates the BranchMap for Turn based on the frenzyFrenzy and on currentPlayer's damages
     */
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

    /**
     * This method restore the situation to the beginning of the last move,
     * it's necessary in case the Player reach an impossible state
     */
    private void rollback() {
        match.rollback(clonedPlayers, clonedDeadPlayers);
        createBranchMap();
    }

    /**
     * This method clones all the Players of the Match
     */
    private void clonePlayers() {
        clonedPlayers = new ArrayList<>();
        clonedDeadPlayers = new ArrayList<>();
        List<Player> deadPlayers = match.getDeadPlayers();
        for (Player p : match.getPlayers()) {
            Player clone = new Player(p);
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

    /**
     * This method returns the list of possible action the currentPlayer can do from the branchMap
     * @return The list of possible action the currentPlayer can do from the branchMap
     */
    @Override
    public List<Action> getActions() {
        List<Action> ret = this.branchMap.getPossibleActions();
        for(Action a : ret)
            a.initialize(currentPlayer);
        return ret;
    }

    /**
     * This method returns the list of possible action the currentPlayer can do from the branchMap in the form of text
     * @return The list of possible action the currentPlayer can do from the branchMap in the form of text
     */
    @Override
    public List<String> getActionTexts() {
        List<String> temp = new ArrayList<>();
        for(int i = 0; i < branchMap.getPossibleActions().size(); i++)
            temp.add("Press" + i + branchMap.getPossibleActions().get(i).getVisualizable().description);
        return temp;
    }
}
