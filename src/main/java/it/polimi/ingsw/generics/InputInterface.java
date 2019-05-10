package it.polimi.ingsw.generics;

import java.io.IOException;

public interface InputInterface
{
    byte[] getBytes() throws IOException;
    <T> T getObject() throws IOException, ClassNotFoundException;
    void getFile(String filename) throws IOException;
    int getInt() throws IOException;
    long getLong() throws IOException;
    boolean getBool() throws IOException;
}
