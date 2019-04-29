package it.polimi.ingsw.generics;

import java.io.*;
import java.nio.ByteBuffer;

public class InputStreamUtils implements Closeable
{
    public final Event<InputStreamUtils, InputStream> streamFailEvent = new Event<>();
    private InputStream stream;
    public InputStreamUtils(InputStream inputStream)
    {
        this.stream = inputStream;
    }

    public void pipeTo(OutputStream out, long max) throws IOException
    {
        get( ()-> InputStreamUtils.pipe(stream, out, max));
    }

    public int getByte() throws IOException
    {
        return get( () -> stream.read());
    }

    public byte[] getBytes(long max) throws IOException
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
            T ret = (T) inputStream.readObject();
            inputStream.close();
            return ret;});
    }

    public void getFileAuto(String filename) throws IOException
    {
        get( ()-> getFile(filename, getDim()));
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

    public byte[] getBytesAuto() throws IOException
    {
        return get( ()-> getBytes(getDim()));
    }

    public void getFile(String saveFilename, long fileSize) throws IOException
    {
        get( ()-> {
            try(FileOutputStream fileStream = new FileOutputStream(saveFilename)){
                this.pipeTo(fileStream, fileSize); }
        });
    }

    private long getDim() throws IOException
    {
        return bytesToLong(getBytes(Long.BYTES));
    }
    private static long bytesToLong(byte[] bytes)
    {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }

    private <T, E extends Exception> T get(GetInterface<T, E> getFunc) throws IOException, E
    {
        try
        {
            return getFunc.get();
        }
        catch(IOException ecc)
        {
            streamFailEvent.invoke(this, stream);
            throw ecc;
        }
    }

    private void get(StreamActionInterface getFunc) throws IOException
    {
        try
        {
            getFunc.invoke();
        }
        catch(IOException ecc)
        {
            streamFailEvent.invoke(this, stream);
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
