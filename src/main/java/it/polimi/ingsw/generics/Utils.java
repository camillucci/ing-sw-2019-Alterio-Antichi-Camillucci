package it.polimi.ingsw.generics;

import java.io.IOException;
import java.rmi.RemoteException;

public class Utils
{
    public static void tryDo(SafeAction<IOException, RemoteException, ClassNotFoundException> safeAction)
    {
        try{
            safeAction.invoke();
        }
        catch(IOException e){
            //todo
        } catch (ClassNotFoundException e) {
            //todo
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
