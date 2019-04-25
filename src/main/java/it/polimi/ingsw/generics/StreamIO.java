package it.polimi.ingsw.generics;

import java.io.*;
import java.nio.ByteBuffer;

public class StreamIO
{
    private StreamIO(){}
    public static void sendByteAuto(OutputStream stream, byte b) throws IOException
    {
        sendDim(stream, Byte.BYTES);
        stream.write(b);
    }

    public static void sendBytesAuto(OutputStream stream, byte[] bytes) throws IOException
    {
        sendDim(stream, bytes.length);
        stream.write(bytes);
    }

    public static void sendFile(OutputStream stream, String filename) throws FileNotFoundException, IOException
    {
        FileInputStream fileStream = new FileInputStream(filename);
        final int buflen = 1024*16;
        byte[] buffer = new byte[buflen];
        int read;
        while((read = fileStream.read(buffer, 0, buflen)) > 0)
            stream.write(buffer,0, read);
        fileStream.close();
    }

    public static void sendFileAuto(OutputStream stream, String filename) throws IOException
    {
        long fileSize = (new File(filename)).length();
        sendDim(stream, fileSize);
        sendFile(stream, filename);
    }

    public static byte[] getBytes (InputStream stream, long max) throws IOException
    {
        if(max < 0)
            return null;
        final int buflen = 1024*16;
        byte[] buffer = new byte[buflen];
        ByteArrayOutputStream ms = new ByteArrayOutputStream();
        int read, min;
        do{
            min = buflen < max ? buflen : (int)max;
            read = stream.read(buffer, 0, min);
            ms.write(buffer, 0, read);
            max -= read;
        } while(max > 0 && read > 0);
        return ms.toByteArray();
    }
    public static byte[] getBytesAuto(InputStream stream) throws IOException
    {
        return getBytes(stream, getDim(stream));
    }

    public static void getFile(InputStream stream, String saveFilename, long fileSize) throws FileNotFoundException, IOException
    {
        FileOutputStream fileStream = new FileOutputStream(saveFilename);
        final int buflen = 16*1024;
        byte[] buffer = new byte[buflen];
        int justRecived = 0;
        for(; (justRecived = stream.read(buffer,0, buflen)) > 0 && fileSize >  0; fileSize -= justRecived)
            fileStream.write(buffer, 0, justRecived);
        fileStream.close();
    }

    public static void getFileAuto(InputStream stream, String filename) throws IOException
    {
        getFile(stream, filename, getDim(stream));
    }

    private static void sendDim(OutputStream stream, long dim) throws IOException
    {
        stream.write(longToBytes(dim));
    }

    private static long getDim(InputStream stream) throws IOException
    {
        return bytesToLong(getBytes(stream, Long.BYTES));
    }


    private static byte[] longToBytes(long val)
    {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(val);
        return buffer.array();
    }

    private static long bytesToLong(byte[] bytes)
    {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }
}
