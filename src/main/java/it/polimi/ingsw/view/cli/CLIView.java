package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.view.View;

public class CLIView extends View
{
    public CLIView() {
        super(new LoginCLI(), new ActionHandlerCLI());
    }
}
