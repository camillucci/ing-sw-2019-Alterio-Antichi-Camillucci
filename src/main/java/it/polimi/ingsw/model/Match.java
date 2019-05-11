package it.polimi.ingsw.model;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.branch.*;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Match extends ActionsProvider {

    public final IEvent<Match, List<Player>> endMatchEvent = new Event<>();
    private int turnPos = -1;
    private List<Action> curActions;
    private Player curPlayer;
    public final GameBoard gameBoard;
    private List<Player> players = new ArrayList<>();
    private List<Player> deadPlayers;
    private List<PlayerColor> playerColors;
    private boolean finalFrenzy = false;
    private int frenzyStarter = -1;
    private static final int MAX_DAMAGES = 12;

    public Match(List<String> playersName, List<PlayerColor> playerColors, int gameLength, int gameSize)
    {
        this.playerColors = new ArrayList<>(playerColors);
        this.gameBoard = new GameBoard(gameLength, gameSize);
        createPlayers(playersName, playerColors);
    }

    private void createPlayers(List<String> playersName, List<PlayerColor> playerColors)
    {
        for(int i = 0; i < playersName.size(); i++) {
            Player p = new Player(playersName.get(i), playerColors.get(i), gameBoard);
            p.deathEvent.addEventHandler((s,a)->this.deadPlayers.add(s));
            p.damagedEvent.addEventHandler(this::onPlayerDamaged);
            players.add(p);
        }
        gameBoard.setPlayers(players);
        this.deadPlayers = new ArrayList<>(players);
    }

    private void onPlayerDamaged(Player damaged, int val)
    {
        List<Action> backupActions = this.curActions;
        BranchMap branchMap = BranchMapFactory.counterAttackBranchMap();
        branchMap.newActionsEvent.addEventHandler((a,actions) -> setNewActions(actions));
        branchMap.endOfBranchMapReachedEvent.addEventHandler((a,b) -> setNewActions(backupActions));
        setNewActions(branchMap.getPossibleActions());
        //todo rollback
    }

    private void spawn(boolean respawn)
    {
        //At spawn players must be spawned one at the time, at respawn all together
        Player p = deadPlayers.get(0);
        this.curPlayer = p;
        p.addPowerUpCardRespawn();
        if(!respawn)
            p.addPowerUpCard();
        BranchMap branchMap = BranchMapFactory.spawnBranchMap(p);
        branchMap.newActionsEvent.addEventHandler((bMap, actions) -> setNewActions(actions));
        if(!respawn)
            branchMap.endOfBranchMapReachedEvent.addEventHandler((a, b) -> { this.deadPlayers.remove(p); newTurn(); });
        else
            branchMap.endOfBranchMapReachedEvent.addEventHandler((a, b) -> { this.deadPlayers.remove(p); onTurnCompleted(); });
        setNewActions(branchMap.getPossibleActions());
    }

    private void onTurnCompleted()
    {
        if(deadPlayers.isEmpty())
            newTurn();
        else
            spawn(turnPos >= players.size());
    }

    private void newTurn()
    {
        turnPos = (turnPos + 1) % players.size();
        curPlayer = players.get(turnPos);
        Turn currentTurn = new Turn(curPlayer, this);
        currentTurn.newActionsEvent.addEventHandler((turn, actions) -> this.setNewActions(actions));
        currentTurn.endTurnEvent.addEventHandler((turn, turnPlayer) -> onTurnCompleted());
        setNewActions(currentTurn.getActions());
    }

    private void setNewActions(List<Action> actions)
    {
        actions.forEach(a->a.initialize(curPlayer));
        this.curActions = actions;
        ((Event<Player, List<Action>>)newActionsEvent).invoke(this.curPlayer, this.curActions);
    }

    public void assignPoints(){
        List<Double> tempCount = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0);
        for (Player deadPlayer : deadPlayers) {
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
    }

    private void updateKillShotTrack(List<PlayerColor> damage, Player deadPlayer) {
        List<PlayerColor> tempKillShot = new ArrayList<>();
        tempKillShot.add(damage.get(MAX_DAMAGES - 2));
        if (damage.size() == MAX_DAMAGES)
            tempKillShot.add(damage.get(MAX_DAMAGES - 1));
        gameBoard.addKillShotTrack(tempKillShot);
        if(gameBoard.getKillShotTrack().size() == 8 && frenzyStarter == -1) {
            finalFrenzy = true;
            frenzyStarter = playerColors.indexOf(deadPlayer.getDamage().get(10));
        }
    }

    public MatchSnapshot createSnapshot(int numPlayer) {
        return new MatchSnapshot(this, players.get(numPlayer));
    }

    public void addDeadPlayers(Player deadPlayer) {
        this.deadPlayers.add(deadPlayer);
    }

    public void rollback(List<Player> clonedPlayers, List<Player> clonedDeadPlayers) {
        players = clonedPlayers;
        deadPlayers = clonedDeadPlayers;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public List<Player> getDeadPlayers() {
        return new ArrayList<>(deadPlayers);
    }

    public boolean getFinalFrenzy() {
        return finalFrenzy;
    }

    public int getFrenzyStarter() {
        return frenzyStarter;
    }

    public int getPlayerIndex() {
        return players.indexOf(curPlayer);
    }

    public void start() {
        spawn(false);
    }

    @Override
    public Player getPlayer() {
        return this.curPlayer;
    }

    @Override
    public List<Action> getActions() {
        return new ArrayList<>(this.curActions);
    }
}
