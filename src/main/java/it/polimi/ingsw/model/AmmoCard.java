package it.polimi.ingsw.model;

public class AmmoCard {

    private int blue;
    private int yellow;
    private int red;
    private PowerUpCard puc;


    public void AmmoCard(int blue, int yellow, int red) {

        this.blue = blue;
        this.yellow = yellow;
        this.red = red;
        puc = NULL;
    }

    public void AmmoCard(int blue, int yellow, int red, PowerUpCard puc) {

        this.blue = blue;
        this.yellow = yellow;
        this.red = red;
        this.puc = puc;
    }

}
