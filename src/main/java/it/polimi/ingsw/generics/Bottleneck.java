package it.polimi.ingsw.generics;

public class Bottleneck
{
    public final IEvent<Bottleneck, Exception> exceptionGenerated = new Event<>();
    public void tryDo(ThrowingAction f)
    {
        try{
            f.invoke();
        } catch (Exception e) {
            e.printStackTrace(); //todo
            ((Event<Bottleneck, Exception>)exceptionGenerated).invoke(this, e);
        }
    }
}
