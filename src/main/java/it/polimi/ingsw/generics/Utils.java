package it.polimi.ingsw.generics;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils
{
    /*
    public static void tryDo(ThrowingAction<IOException, RemoteException, ClassNotFoundException> ThrowingAction)
    {
        try{
            ThrowingAction.invoke();
        }
        catch(IOException | ClassNotFoundException e) {
            Logger.getLogger("utils").log(Level.WARNING, e.getMessage());
        }
    }

    public static  <T> T tryGet(SafeSupplier<IOException, RemoteException, T> safeSupplier)
    {
        try{
            return safeSupplier.get();
        }
        catch(IOException e){
            Logger.getLogger("utils.tryGet()").log(Level.WARNING, e.getMessage());
            return null;
        }
    }

    public static void newThread(Runnable tryDo){
        (new Thread(tryDo)).start();
    }

     */
}
