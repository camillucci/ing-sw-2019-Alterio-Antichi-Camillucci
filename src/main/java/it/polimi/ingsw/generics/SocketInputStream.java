package it.polimi.ingsw.generics;

import java.io.*;
import java.nio.ByteBuffer;

public class SocketInputStream implements Closeable
{
    public final IEvent<SocketInputStream, SocketInputStream> streamFailEvent = new Event<>();
    private InputStream stream;
    public static final int DATA = 0;
    public static final int PING = 1;
    private int count = 0;

    public SocketInputStream(InputStream inputStream)
    {
        this.stream = inputStream;
    }

    public void pipeTo(OutputStream out, long max) throws IOException
    {
        get( ()-> SocketInputStream.pipe(stream, out, max));
    }

    public int getByteOnly() throws IOException
    {
        return get( () -> stream.read());
    }

    public byte[] getBytesOnly(long max) throws IOException
    {
        return get( () -> {
            ByteArrayOutputStream ms = new ByteArrayOutputStream();
            this.pipeTo(ms, max);
            return ms.toByteArray();});
    }

    public  <T> T getObject() throws IOException, ClassNotFoundException
    {
        return get ( () -> {
            ObjectInputStream inputStream = new ObjectInputStream(stream);
            return (T) inputStream.readObject();});
    }

    public void getFile(String filename) throws IOException
    {
        get( ()-> getFileOnly(filename, getLong()));
    }

    public static void pipe(InputStream in, OutputStream out, long max) throws IOException
    {
        if(max < 0)
            return;
        final int buflen = 1024*16;
        byte[] buffer = new byte[buflen];
        int read;
        int min;
        do{
            min = buflen < max ? buflen : (int)max;
            read = in.read(buffer, 0, min);
            if(read <= 0)
                break;
            out.write(buffer, 0, read);
            max -= read;
        } while(max > 0);
    }

    public byte[] getBytes() throws IOException
    {
        return get( ()-> getBytesOnly(getLong()));
    }

    public void getFileOnly(String saveFilename, long fileSize) throws IOException
    {
        get( ()-> {
            try(FileOutputStream fileStream = new FileOutputStream(saveFilename)){
                this.pipeTo(fileStream, fileSize); }
        });
    }

    public long getLong() throws IOException
    {
        return get( () ->bytesToLong(getBytesOnly(Long.BYTES)));
    }

    public int getInt() throws IOException
    {
        return get( ()-> bytesToInt(getBytesOnly(Integer.BYTES)));
    }

    public boolean getBool() throws IOException
    {
        int b = getByteOnly();
        switch (b)
        {
            case -1:
                throw new RuntimeException("impossible to get a bool from the Input Stream");
            case 0:
                return false;
            default:
                return true;
        }
    }
    private static long bytesToLong(byte[] bytes)
    {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }

    private static int bytesToInt(byte[] bytes)
    {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getInt();
    }

    private synchronized  <T, E extends Exception> T get(GetInterface<T, E> getFunc) throws IOException, E
    {
        try
        {
            if(count == 0)
                while (stream.read() == PING)
                    ;
            count++;
            T ret = getFunc.get();
            count--;
            return  ret;
        }
        catch(IOException ecc)
        {
            ((Event<SocketInputStream, SocketInputStream>)streamFailEvent).invoke(this, this);
            throw ecc;
        }
    }

    private synchronized void get(StreamActionInterface getFunc) throws IOException
    {
        try
        {
            if(count == 0)
                while (stream.read() != DATA)
                    ;
            count++;
            getFunc.invoke();
            count--;
        }
        catch(IOException ecc)
        {
            ((Event<SocketInputStream, SocketInputStream>)streamFailEvent).invoke(this, this);
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

    @FunctionalInterface
    private interface GetInterface<T, E extends Exception>
    {
        T get() throws IOException, E;
    }
}
