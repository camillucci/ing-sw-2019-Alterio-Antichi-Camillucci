package it.polimi.ingsw.model;

public class Player {

    private String name;
    private int points;
    private int blueAmmo;
    private int yellowAmmo;
    private int redAmmo;
    private int skull;
    private PlayerColor color;
    private ArrayList<PlayerColor> damage;
    private ArrayList<PlayerColor> mark;
    private ArrayList<WeaponCard> weapons;
    private ArrayList<PowerUpCard> powerups;
    private ArrayList<PlayerDeathSubscriber> subscribers;
    private GameBoard board;
    private Square currentSquare;

    public void Player (String name) {

        this.name = name;
        arraylist<playerDeathSubscriber> subscribers = new playerDeathSubscriber();

    }

   public void addDeathSubscriber () {
        //TODO//
   }

}
