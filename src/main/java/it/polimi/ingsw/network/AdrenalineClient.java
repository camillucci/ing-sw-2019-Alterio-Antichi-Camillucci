package it.polimi.ingsw.network;

import it.polimi.ingsw.view.cli.ManagerCLI;

public class AdrenalineClient
{
    private Client server;
    private ManagerCLI managerCLI = new ManagerCLI();

    public AdrenalineClient(Client server)
    {
        this.server = server;
    }

}
