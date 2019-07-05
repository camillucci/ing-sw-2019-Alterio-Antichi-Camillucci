package it.polimi.ingsw.model;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.CounterPowerUpAction;
import it.polimi.ingsw.model.branch.*;
import it.polimi.ingsw.snapshots.MatchSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class represent he entire Match, it contains all the references to the Players,
 * the GameBoard, the current Turn and the possible Actions.
 * It directly communicate with the Controller for the Action that can be done by the currently playing Player
 * and it updates the whole game state when necessary. It also notify the Controller when the Match is over
 */
public class Match extends ActionsProvider {

    /**
     * The event that notifies the Controller when the Match is over
     */
    public final IEvent<Match, List<Player>> endMatchEvent = new Event<>();
    /**
     * The current turn progressive number, it stars at -1 which means that all Players must spawn for the first time
     */
    private int turnPos = -1;
    /**
     * The list of doable action of the currently playing Player
     */
    private List<Action> curActions;
    /**
     * The Player currently playing
     */
    private Player curPlayer;
    /**
     * The GameBoard through which the Squares are updated
     */
    private GameBoard gameBoard;
    /**
     * The list of the Players playing the match, the order is never changed
     */
    private List<Player> players = new ArrayList<>();
    /**
     * The temporary list of dead Players, which need to respawn at the end of the current Turn
     */
    private List<Player> deadPlayers;
    /**
     * The list of color associated with the Players, the order is never changed
     */
    private final List<PlayerColor> playerColors;
    /**
     * It state if the final frenzy modality has started
     */
    private boolean finalFrenzy = false;
    /**
     * It contains the index of the player that started the final frenzy
     */
    private int frenzyStarter = -1;
    /**
     * The BranchMap used for the CounterAttackPowerUpCard
     */
    private List<BranchMap> counterAttackBranchMap = new ArrayList<>();
    /**
     * The Player that is using the counterAttackBranchMap
     */
    private List<Player> counterAttackPlayer = new ArrayList<>();
    /**
     * The list of Players cloned at the beginning of each move, it's used for the rollback
     */
    private List<Player> clonedPlayers;
    /**
     * The list of dead Player cloned at the beginning of each move, it's used for the rollback
     */
    private List<Player> clonedDeadPlayers;
    /**
     * The GameBoard cloned at the beginning of each move, it's used for the rollback
     */
    private GameBoard clonedGameBoard;
    private static final int MAX_DAMAGES = 12;
    private boolean spawning = false;
    private boolean stopSendingActions = false;
    private Turn currentTurn;

    /**
     * Given the names and the colors of the Players, chosen through the Client, it creates the GameBoard and all Players
     * @param playersName The list of the name the Players chose
     * @param playerColors The list of the colors the Players chose
     * @param gameLength The number of skulls, from 5 to 8
     * @param mapType The map type, from 0 to 3
     */
    public Match(List<String> playersName, List<PlayerColor> playerColors, int gameLength, int mapType)
    {
        this.playerColors = new ArrayList<>(playerColors);
        this.gameBoard = new GameBoard(gameLength, mapType);
        createPlayers(playersName, playerColors);
    }

    /**
     * This method creates all the Players that are playing this Match
     * @param playersName The Players names
     * @param playerColors The Players colors
     */
    private void createPlayers(List<String> playersName, List<PlayerColor> playerColors)
    {
        for(int i = 0; i < playersName.size(); i++) {
            Player p = new Player(playersName.get(i), playerColors.get(i), gameBoard);
            p.deathEvent.addEventHandler((s,a)->{
                if(!deadPlayers.contains(s))
                    this.deadPlayers.add(s);
            });
            p.damagedEvent.addEventHandler(this::onPlayerDamaged);
            players.add(p);
        }
        gameBoard.setPlayers(players);
        this.deadPlayers = new ArrayList<>(players);
    }

    /**
     * This method spawn the Players during the first round of the game
     * and respawn them at the end of the turn in which they died
     * @param respawn Is false if the player need to spawn for the first time, true otherwise
     */
    private void spawn(boolean respawn)
    {
        spawning = true;
        clonePlayersAndGameBoard();
        Player p = deadPlayers.get(0);
        if(respawn) {
            assignPoints(p);
            p.setSkull(p.getSkull() + 1);
            p.clearDamage();
            if(finalFrenzy)
                p.setFinalFrenzy();
        }

        //p.addDamageNoMarks(players.get(0), 10);
        this.curPlayer = p;
        p.addPowerUpCardRespawn();
        if(!respawn)
            p.addPowerUpCard();
        BranchMap branchMap = BranchMapFactory.spawnBranchMap(p);
        branchMap.newActionsEvent.addEventHandler((bMap, actions) -> setNewActions(actions));
        branchMap.rollbackEvent.addEventHandler((bMap, rollbackAction) -> rollback(respawn));
        branchMap.endOfBranchMapReachedEvent.addEventHandler((a, b) -> {
            for(Player player : deadPlayers)
                if(player.color.equals(p.color)) {
                    this.deadPlayers.remove(player);
                    break;
                }
            spawning = false;
        });
        if(!respawn)
            branchMap.endOfBranchMapReachedEvent.addEventHandler((a, b) -> newTurn());
        else
            branchMap.endOfBranchMapReachedEvent.addEventHandler((a, b) -> onTurnCompleted());
        setNewActions(branchMap.getPossibleActions());
    }



    /**
     * This method is called when a Turn is over, it spawns all the dead Players and starts the next Turn
     */
    private void onTurnCompleted()
    {
        for(Square square : gameBoard.getSquares())
            square.refill();
        if(finalFrenzy && frenzyStarter == players.indexOf(curPlayer)) {
            for(Player player : deadPlayers)
                assignPoints(player);
            ((Event<Match, List<Player>>)endMatchEvent).invoke(this, players);
        }
        if(deadPlayers.isEmpty())
            newTurn();
        else
            spawn(turnPos >= players.size() - 1);
    }

    /**
     * This method initializes and starts a new Turn
     */
    private void newTurn()
    {
        turnPos = turnPos + 1;
        curPlayer = players.get(turnPos % players.size());
        currentTurn = new Turn(curPlayer, this);
        currentTurn.newActionsEvent.addEventHandler((player, actions) -> this.setNewActions(actions));
        currentTurn.endTurnEvent.addEventHandler((turn, turnPlayer) -> onTurnCompleted());
        setNewActions(currentTurn.getActions());
    }

    /**
     * This method set on the curAction parameter the Actions that can be done by the current playing Player
     * and invoke the event which notify the current Player about the action that can be done
     * @param actions The action that can be done by the current playing Player
     */
    private void setNewActions(List<Action> actions)
    {
        if(!counterAttackBranchMap.isEmpty())
        {
            List<Action> backupActions = actions;
            Player backupPlayer = curPlayer;
            counterAttackBranchMap.get(0).newActionsEvent.addEventHandler((branchMap, actionList) -> setNewActions(actionList));
            counterAttackBranchMap.get(0).endOfBranchMapReachedEvent.addEventHandler((a, b) -> {
                this.curPlayer = backupPlayer;
                setNewActions(backupActions);
            });
            actions = counterAttackBranchMap.get(0).getPossibleActions();
            curPlayer = counterAttackPlayer.get(0);

            counterAttackBranchMap.remove(counterAttackBranchMap.get(0));
            counterAttackPlayer.remove(counterAttackPlayer.get(0));
        }
        actions.forEach(a->a.initialize(curPlayer));
        this.curActions = actions;
        if(!stopSendingActions)
            ((Event<Player, List<Action>>)newActionsEvent).invoke(this.curPlayer, this.curActions);
    }

    /**
     * This method is called through an event when a Player is damaged,
     * it creates a new BranchMap that makes the damaged player choose if it wants to activate a CounterAttackPowerUpCard
     * @param damaged The damaged Player
     */
    private void onPlayerDamaged(Player damaged, Player shooter)
    {
        if(!damaged.getPowerupSet().getCounterAttackPUs().isEmpty()) {
            counterAttackPlayer.add(damaged);
            BranchMap branchMap = BranchMapFactory.counterAttackBranchMap();
            CounterPowerUpAction counterPowerUpAction = (CounterPowerUpAction)branchMap.getPossibleActions().get(0);
            counterPowerUpAction.setTargets(Collections.singletonList(shooter));
            counterAttackBranchMap.add(branchMap);
        }
    }

    /**
     * This method assign the points to Players whenever a Player is killed, before it respawn
     */
    public void assignPoints(Player deadPlayer){
        List<Double> tempCount = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0);
        List<PlayerColor> damage = deadPlayer.getDamage();
        int tempSkull = deadPlayer.getSkull();
        if (!deadPlayer.isFinalFrenzy())
            players.get(playerColors.indexOf(damage.get(0))).addPoints(1);
        for (int j = 0; j < damage.size(); j++)
            tempCount.set(playerColors.indexOf(damage.get(j)), tempCount.get(playerColors.indexOf(damage.get(j))) + 1 + Math.pow(2, (double) MAX_DAMAGES - j) / 10000);
        for (int j = 0; j < players.size() - 1 && Collections.max(tempCount) > 0.0; j++) {
            if (!deadPlayer.isFinalFrenzy())
                players.get(tempCount.indexOf(Collections.max(tempCount))).addPoints(Math.max(8 - tempSkull * 2 - j * 2, 1));
            else
                players.get(tempCount.indexOf(Collections.max(tempCount))).addPoints(Math.max(2 - tempSkull * 2 - j * 2, 1));
            tempCount.set(tempCount.indexOf(Collections.max(tempCount)), 0.0);
        }
        updateKillShotTrack(damage, deadPlayer);
    }

    /**
     * This method updates the kill shot track whenever a player is killed and its points are assigned,
     * it also updates finalFrenzy and the frenzyStarter when needed
     * @param damage The new kill shot track to addTarget
     * @param deadPlayer The dead player that is being evaluated
     */
    private void updateKillShotTrack(List<PlayerColor> damage, Player deadPlayer) {
        List<PlayerColor> tempKillShot = new ArrayList<>();
        tempKillShot.add(damage.get(MAX_DAMAGES - 2));
        if (damage.size() == MAX_DAMAGES) {
            tempKillShot.add(damage.get(MAX_DAMAGES - 1));
            players.get(playerColors.indexOf(damage.get(MAX_DAMAGES - 1))).addMark(deadPlayer, 1);
        }
        gameBoard.addKillShotTrack(tempKillShot);
        if(gameBoard.getKillShotTrack().size() == 8 && frenzyStarter == -1) {
            finalFrenzy = true;
            frenzyStarter = playerColors.indexOf(deadPlayer.getDamage().get(10));
        }
    }

    /**
     * This method creates a MatchSnapshot of the current game state
     * @param numPlayer The index of the Player the MatchSnapShot will be sent to
     * @return The MatchSnapshot of the current game state
     */
    public MatchSnapshot createSnapshot(int numPlayer) {
        return new MatchSnapshot(this, players.get(numPlayer));
    }

    /**
     * This method adds a new dead Player to the deadPlayers list
     * @param deadPlayer The dead Player to addTarget
     */
    public void addDeadPlayers(Player deadPlayer) {
        this.deadPlayers.add(deadPlayer);
    }

    /**
     * This method clones all the Players of the Match and the GameBoard
     */
    public void clonePlayersAndGameBoard() {
        boolean alreadyAdded = false;
        clonedGameBoard = new GameBoard(gameBoard);
        clonedPlayers = clonedGameBoard.getPlayers();
        for(Player player : deadPlayers) {
            for(Player p : clonedPlayers)
                if(player.color.equals(p.color))
                    alreadyAdded = true;
            if(!alreadyAdded)
                clonedPlayers.add(new Player(player, clonedGameBoard, null));
            else
                alreadyAdded = false;
        }

        clonedDeadPlayers = new ArrayList<>();
        for(Player p : deadPlayers) {
            for(Player player : clonedPlayers)
                if(player.color.equals(p.color)) {
                    clonedDeadPlayers.add(player);
                    break;
                }
        }
    }

    /**
     * This method restore the last precedent legal game state
     */
    public void rollback() {
        int index = players.indexOf(curPlayer);
        this.players = new ArrayList<>();
        for(PlayerColor playerColor : playerColors)
            for(Player player : clonedPlayers)
                if(player.color.equals(playerColor)) {
                    players.add(player);
                    break;
                }
        this.deadPlayers = clonedDeadPlayers;
        this.gameBoard = clonedGameBoard;
        gameBoard.setPlayers(players);
        curPlayer = players.get(index);
    }

    private void rollback(Boolean respawn) {
        rollback();
        spawn(respawn);
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public boolean getFinalFrenzy() {
        return finalFrenzy;
    }

    public int getFrenzyStarter() {
        return frenzyStarter;
    }

    /**
     * This method starts the game after it is created
     */
    public void start() {
        spawn(false);
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    @Override
    public Player getPlayer() {
        return curPlayer;
    }

    @Override
    public List<Action> getActions() {
        return new ArrayList<>(this.curActions);
    }

    @Override
    public List<String> getActionTexts() {
        List<String> temp = new ArrayList<>();
        for(int i = 0; i < curActions.size(); i++)
            temp.add("Press" + i + curActions.get(i).getVisualizable().description);
        return temp;
    }

    public void skipTurn() {
        stopSendingActions = true;
        if(spawning)
        {
            while(spawning)
                getActions().get(0).doAction();
            stopSendingActions = false;
            onTurnCompleted();
        }
        else if(getPlayer() != currentTurn.getPlayer())
        {
            stopSendingActions = false;
            getActions().get(1).doAction();
        }
        else
        {
            stopSendingActions = false;
            onTurnCompleted();
        }
    }
}
