package it.polimi.ingsw.generics;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils
{
    public static void tryDo(SafeAction<IOException, RemoteException, ClassNotFoundException> safeAction)
    {
        try{
            safeAction.invoke();
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
            //todo
            return null;
        }
    }
}
