package it.polimi.ingsw.network;

public class Client {
    private String name;
    private boolean onTurn = false;
    private boolean connectionType;
    private boolean interfaceType;

    public boolean getOnTurn() {
        return onTurn;
    }

    public void setOnTurn(boolean onTurn) {
        this.onTurn = onTurn;
        return;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setConnection(boolean connectionType) {
        this.connectionType = connectionType;
    }

    public void setInterface(boolean interfaceType) {
        this.interfaceType = interfaceType;
    }
}
