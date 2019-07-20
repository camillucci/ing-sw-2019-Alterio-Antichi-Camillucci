package it.polimi.ingsw.generics;

/**
 * Utility class with the goal of throwing a default exception when a ThrowingAction is invoked and it fails.
 */
public class Bottleneck
{
    /**
     * Event used to handle all the cases where subscribers are expecting a method to possibly fail
     */
    public final IEvent<Bottleneck, Exception> exceptionGenerated = new Event<>();

    /**
     * Invokes the generic function gotten as input. If it fails, the exception event is invoked, notifying all the
     * subscribers
     * @param f Function that needs to be executed
     */
    public void tryDo(ThrowingAction f)
    {
        try{
            f.invoke();
        } catch (Exception e) {
            ((Event<Bottleneck, Exception>)exceptionGenerated).invoke(this, e);
        }
    }
}
