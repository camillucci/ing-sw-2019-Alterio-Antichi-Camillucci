package it.polimi.ingsw.generics;

import java.io.Serializable;

public interface OutputInterface
{
    void sendBytes(byte[] bytes) throws Exception;
    void sendObject(Serializable object) throws Exception;
    void sendFile(String fileName) throws Exception;
    void sendInt(int val) throws Exception;
    void sendLong(long val) throws Exception;
    void sendBool(boolean bool) throws Exception;
}
