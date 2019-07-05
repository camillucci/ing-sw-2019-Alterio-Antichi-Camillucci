package it.polimi.ingsw.generics;

import java.util.logging.Logger;

/**
 * Utility class with the goal of throwing a default exception when a ThrowingAction is invoked and it fails.
 */
public class Bottleneck
{
    public final IEvent<Bottleneck, Exception> exceptionGenerated = new Event<>();

    public void tryDo(ThrowingAction f)
    {
        try{
            f.invoke();
        } catch (Exception e) {
            ((Event<Bottleneck, Exception>)exceptionGenerated).invoke(this, e);
        }
    }
}
