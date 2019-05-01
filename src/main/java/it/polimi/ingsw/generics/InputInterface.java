package it.polimi.ingsw.generics;

public interface InputInterface
{
    byte[] getBytes() throws Exception;
    <T> T getObject() throws Exception;
    void getFile(String filename) throws Exception;
    int getInt() throws Exception;
    long getLong() throws Exception;
    boolean getBool() throws Exception;
}
