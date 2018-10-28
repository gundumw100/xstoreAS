package com.base.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import android.util.Log;

/**
 * @author minrui
 * @date 2014-10-23
 */
@Deprecated
class MultipartEntity implements HttpEntity
{
    private final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private String boundary = null;

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    boolean isSetLast = false;

    boolean isSetFirst = false;

    public MultipartEntity()
    {
        final StringBuffer buf = new StringBuffer();
        final Random rand = new Random();
        for (int i = 0; i < 30; i++)
        {
            buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        this.boundary = buf.toString();

    }

    public void writeFirstBoundaryIfNeeds()
    {
        if (!isSetFirst)
        {
            try
            {
                out.write(("--" + boundary + "\r\n").getBytes());
            }
            catch (final IOException e)
            {
                e.printStackTrace();
            }
        }

        isSetFirst = true;
    }

    public void writeLastBoundaryIfNeeds()
    {
        if (isSetLast)
        {
            return;
        }

        try
        {
            out.write(("\r\n--" + boundary + "--\r\n").getBytes());
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }

        isSetLast = true;
    }

    public void addPart(final String key, final String value)
    {
        writeFirstBoundaryIfNeeds();
        try
        {
            out.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n").getBytes());
            out.write(value.getBytes());
            out.write(("\r\n--" + boundary + "\r\n").getBytes());
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
    }

    public void addPart(final String key, final String fileName, final InputStream fin, final boolean isLast)
    {
        addPart(key, fileName, fin, "application/octet-stream", isLast);
    }

    public void addPart(final String key, final String fileName, final InputStream fis, String type, final boolean isLast)
    {
        writeFirstBoundaryIfNeeds();
        try
        {
            type = "Content-Type: " + type + "\r\n";
            out.write(("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + fileName + "\"\r\n").getBytes());
            out.write(type.getBytes());
            out.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());

            final byte[] buffer = new byte[4096];
            int len = 0;
            // java.io.IOException: read failed: EBADF (Bad file number)
            // Caused by: libcore.io.ErrnoException: read failed: EBADF (Bad file number)
            // while ((l = fis.read(tmp)) != -1)
            while ((len = fis.read(buffer)) > 0)
            {
                out.write(buffer, 0, len);
            }
            if (!isLast)
                out.write(("\r\n--" + boundary + "\r\n").getBytes());
            else
            {
                writeLastBoundaryIfNeeds();
            }
            out.flush();
            out.close();
            fis.close();
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
        // finally
        // {
        // try
        // {
        // out.close();
        // fin.close();
        // }
        // catch (final IOException e)
        // {
        // e.printStackTrace();
        // }
        // }
    }

    public void addPart(final String key, final File value, final boolean isLast)
    {
        try
        {
            addPart(key, value.getName(), new FileInputStream(value), isLast);
        }
        catch (final FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public long getContentLength()
    {
        writeLastBoundaryIfNeeds();
        return out.toByteArray().length;
    }

    @Override
    public Header getContentType()
    {
        return new BasicHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
    }

    @Override
    public boolean isChunked()
    {
        return false;
    }

    @Override
    public boolean isRepeatable()
    {
        return false;
    }

    @Override
    public boolean isStreaming()
    {
        return false;
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException
    {
        outstream.write(out.toByteArray());
    }

    @Override
    public Header getContentEncoding()
    {
        return null;
    }

    @Override
    public void consumeContent() throws IOException, UnsupportedOperationException
    {
        if (isStreaming())
        {
            throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent()");
        }
    }

    @Override
    public InputStream getContent() throws IOException, UnsupportedOperationException
    {
        return new ByteArrayInputStream(out.toByteArray());
    }
}