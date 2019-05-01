package it.polimi.ingsw.generics;

import java.io.*;
import java.nio.ByteBuffer;

public class OutputStreamUtils implements Closeable, OutputInterface
{
    public final Event<OutputStreamUtils, OutputStream> streamFailedEvent = new Event<>();
    private OutputStream stream;

    public OutputStreamUtils(OutputStream inputStream)
    {
        this.stream = inputStream;
    }

    public void sendByte(byte b) throws IOException
    {
        send( () -> stream.write(b));
    }

    public void sendByteAuto(byte b) throws IOException
    {
        send( () -> {
            sendLong(Byte.BYTES);
            stream.write(b);});
    }

    public void sendBytesOnly(byte[] bytes) throws IOException
    {
        send(()->stream.write(bytes));
    }
    public void sendBytes(byte[] bytes) throws IOException
    {
        send(() -> {
            sendLong(bytes.length);
            stream.write(bytes); });
    }

    public void sendFileOnly(String filename) throws IOException
    {
        send( () -> {
            try (FileInputStream fileStream = new FileInputStream(filename)) {
                InputStreamUtils.pipe(fileStream, stream, Long.MAX_VALUE);
            }});
    }

    public void sendObject(Serializable object) throws IOException
    {
        send( () -> {
            ObjectOutputStream objectStream = new ObjectOutputStream(stream);
            objectStream.writeObject(object); });
    }

    public void sendFile(String filename) throws IOException
    {
        send(()->{
            long fileSize = (new File(filename)).length();
            sendLong(fileSize);
            sendFileOnly(filename);});
    }

    public void sendLong(long val) throws IOException
    {
        send( () -> stream.write(longToBytes(val)));
    }

    public void sendInt(int val) throws IOException
    {
        send( () -> stream.write(intToBytes(val)));
    }

    public void sendBool(boolean bool) throws Exception
    {
        byte b = bool ? (byte)1 : (byte)0;
        sendByte(b);
    }

    private static byte[] longToBytes(long val)
    {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(val);
        return buffer.array();
    }

    private static byte[] intToBytes(int val)
    {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(val);
        return buffer.array();
    }

    private void send(StreamActionInterface sendFunc) throws IOException
    {
        try
        {
            sendFunc.invoke();
        }
        catch(IOException ecc)
        {
            streamFailedEvent.invoke(this, stream);
            throw ecc;
        }
    }

    @Override
    public void close() throws IOException
    {
        stream.close();
    }

    @FunctionalInterface
    private interface StreamActionInterface
    {
        void invoke() throws IOException;
    }
}
