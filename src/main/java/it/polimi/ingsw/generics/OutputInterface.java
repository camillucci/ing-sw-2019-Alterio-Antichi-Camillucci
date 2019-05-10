package it.polimi.ingsw.generics;

import java.io.IOException;
import java.io.Serializable;

public interface OutputInterface
{
    void sendBytes(byte[] bytes) throws IOException;
    void sendObject(Serializable object) throws IOException;
    void sendFile(String fileName) throws IOException;
    void sendInt(int val) throws IOException;
    void sendLong(long val) throws IOException;
    void sendBool(boolean bool) throws IOException;
}
