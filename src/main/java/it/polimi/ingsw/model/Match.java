package it.polimi.ingsw.model;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.branch.BranchMap;
import it.polimi.ingsw.model.branch.BranchMapFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Match implements ActionsProvider {
    public  final Event<Player, List<Action>> newActionsEvent = new Event<>();
    public final Event<Match, List<Player>> endMatchEvent = new Event<>();

    private int turnPos = 0;
    private List<Action> curActions;
    private Player curPlayer;
    private GameBoard gameBoard;
    private List<Player> players = new ArrayList<>();
    private List<Player> deadPlayers = new ArrayList<>();
    private Turn currentTurn;
    private List<PlayerColor> playerColors;
    private boolean finalFrenzy;
    private int gameLength;
    private int gameSize;
    private int frenzyStarter;
    private static final int MAX_DAMAGES = 12;

    public Match(List<String> playersName, List<PlayerColor> playerColors, int gameLength, int gameSize) {

        this.gameLength = gameLength;
        this.gameSize = gameSize;
        this.finalFrenzy = false;
        this.playerColors = new ArrayList<>(playerColors);
        this.gameBoard = new GameBoard(gameLength, gameSize);
        createPlayers(playersName, playerColors);
        newTurn();
    }

    private void createPlayers(List<String> playersName, List<PlayerColor> playerColors)
    {
        for(int i = 0; i < playersName.size(); i++) {
            Player p = new Player(playersName.get(i), playerColors.get(i), gameBoard);
            p.deathEvent.addEventHandler((s,a)->this.deadPlayers.add(s));
            players.add(p);
        }
        gameBoard.setPlayers(players);
        //this.deadPlayers = new ArrayList<>(players); TODO uncomment this line
        spawn(false);
    }

    private void spawn(boolean respawn)
    {
        if(deadPlayers.isEmpty()) {
            newTurn();
            return;
        }
        Player p = this.curPlayer = deadPlayers.get(0);
        p.addPowerUpCardRespawn();
        if(!respawn)
            p.addPowerUpCardRespawn();
        BranchMap branchMap = BranchMapFactory.spawnBranchMap(p);
        branchMap.newActionsEvent.addEventHandler((bMap, actions)-> setNewActions(actions));
        branchMap.endOfBranchMapReachedEvent.addEventHandler((a, b)->{
            this.deadPlayers.remove(p);
            spawn(respawn);
        } );
        setNewActions(branchMap.getPossibleActions());
    }

    private void OnTurnCompleted(Turn turn, Player turnPlayer)
    {
        spawn(true);
    }

    private void newTurn()
    {
        turnPos = (turnPos + 1) % players.size();
        curPlayer = players.get(turnPos);
        this.currentTurn = new Turn(curPlayer, this);
        this.currentTurn.newActionsEvent.addEventHandler((turn, actions) -> this.setNewActions(actions));
        this.currentTurn.endTurnEvent.addEventHandler(this::OnTurnCompleted);
    }

    private void setNewActions(List<Action> actions)
    {
        actions.forEach(a->a.initialize(curPlayer));
        this.curActions = actions;
        this.newActionsEvent.invoke(this.curPlayer, this.curActions);
    }

    public void assignPoints(){
        List<Double> tempCount = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0);
        for(int i = 0; i < deadPlayers.size(); i++) {
            List<PlayerColor> damage = deadPlayers.get(i).getDamage();
            int tempSkull = deadPlayers.get(i).getSkull();
            if(!deadPlayers.get(i).isFinalFrenzy())
                players.get(playerColors.indexOf(damage.get(0))).addPoints(1);
            for(int j = 0; j < damage.size(); j++)
                tempCount.set(playerColors.indexOf(damage.get(j)), tempCount.get(playerColors.indexOf(damage.get(j))) + 1 + Math.pow(2, (double) MAX_DAMAGES - j) / 10000);
            for(int j = 0; j < players.size() - 1 && Collections.max(tempCount) > 0.0; j++) {
                if(!deadPlayers.get(i).isFinalFrenzy())
                    players.get(tempCount.indexOf(Collections.max(tempCount))).addPoints(Math.max(8 - tempSkull * 2 - j * 2, 1));
                else
                    players.get(tempCount.indexOf(Collections.max(tempCount))).addPoints(Math.max(2 - tempSkull * 2 - j * 2, 1));
                tempCount.set(tempCount.indexOf(Collections.max(tempCount)), 0.0);
            }
            List<PlayerColor> tempKillShot = new ArrayList<>();
            tempKillShot.add(damage.get(MAX_DAMAGES - 2));
            if(damage.size() == MAX_DAMAGES)
                tempKillShot.add(damage.get(MAX_DAMAGES - 1));
            gameBoard.addKillShotTrack(tempKillShot);
        }
    }

    public void addDeadPlayers(Player deadPlayer) {
        this.deadPlayers.add(deadPlayer);
    }

    public void rollback(List<Player> clonedPlayers) {
        players = clonedPlayers;
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

    @Override
    public Player getPlayer() {
        return this.curPlayer;
    }

    @Override
    public List<Action> getActions() {
        return new ArrayList<>(this.curActions);
    }
}
