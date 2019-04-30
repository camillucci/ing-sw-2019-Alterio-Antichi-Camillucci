package it.polimi.ingsw.model;

import it.polimi.ingsw.generics.Pair;
import it.polimi.ingsw.model.action.EndBranchAction;
import it.polimi.ingsw.model.action.PowerUpAction;
import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.weapons.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.model.AmmoColor.*;

public class PowerUpFactory {

    private PowerUpFactory(){}

    private static ArrayList<PowerUpCard> powerUpCards = new ArrayList<>();

    public static List<PowerUpCard> getPowerUps()
    {
        if(powerUpCards.isEmpty())
            buildPowerUps();
        return new ArrayList<>(powerUpCards);
    }

    private static void buildPowerUps() {
        List <AmmoColor> allColors = new ArrayList<>(Arrays.asList(BLUE, RED, YELLOW, BLUE, RED, YELLOW));
        for(AmmoColor ammoColor : allColors) {
            powerUpCards.add(new PowerUpCard("Targeting Scope", ammoColor, new Ammo(0, 0, 0)));
            powerUpCards.add(new PowerUpCard("Newton", ammoColor, new Ammo(0, 0, 0), new Branch(new PowerUpAction(otherPlayers, near2Squares, move), new EndBranchAction())));
            powerUpCards.add(new PowerUpCard("Tagback Grenade", ammoColor, new Ammo(0, 0, 0)));
            powerUpCards.add(new PowerUpCard("Teleporter", ammoColor, new Ammo(0, 0, 0), new Branch(new PowerUpAction(selfPlayer, allSquares, move), new EndBranchAction())));
        }
    }

    //private static ShootFunc damage(Integer ... damage) { return (player, players, squares) -> Effects.damage(player, players, Arrays.asList(damage)); }
    //private static ShootFunc mark(Integer ... marks) { return (player, players, squares) -> Effects.mark(player, players, Arrays.asList(marks)); }
    private static ShootFunc move = (player, players, squares) -> Effects.move(players, squares);

    private static PlayersFilter selfPlayer = (player, players) -> getLeftList(TargetsFilters.teleporterMovableTargets(player));
    private static PlayersFilter otherPlayers = (player, players) -> getLeftList(TargetsFilters.newtonMovableTargets(player));

    private static SquaresFilter allSquares = (player, squares) -> getRightList(TargetsFilters.teleporterMovableTargets(player));
    private static SquaresFilter near2Squares = (player, squares) -> getRightList(TargetsFilters.newtonMovableTargets(player));

    private static List<Player> getLeftList(List<Pair<Player, Square>> pairList) {
        List<Player> temp = new ArrayList<>();
        for(Pair<Player, Square> p : pairList)
            temp.add(p.getLeft());
        return temp;
    }

    private static List<Square> getRightList(List<Pair<Player, Square>> pairList) {
        List<Square> temp = new ArrayList<>();
        for(Pair<Player, Square> p : pairList)
            temp.add(p.getRight());
        return temp;
    }
}
