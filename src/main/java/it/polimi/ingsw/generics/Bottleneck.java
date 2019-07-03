package it.polimi.ingsw.generics;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class with the goal of throwing a default exception when a ThrowingAction is invoked and it fails.
 */
public class Bottleneck
{
    public final IEvent<Bottleneck, Exception> exceptionGenerated = new Event<>();
    private static final Logger logger = Logger.getLogger("Bottleneck");

    public void tryDo(ThrowingAction f)
    {
        try{
            f.invoke();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            ((Event<Bottleneck, Exception>)exceptionGenerated).invoke(this, e);
        }
    }
}
