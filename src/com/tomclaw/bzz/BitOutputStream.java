package com.tomclaw.bzz;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by ivsolkin on 08.06.17.
 */
public class BitOutputStream implements Closeable, Flushable {
    private OutputStream output;
    private int digits;
    private int numDigits;

    private static final int BYTE_SIZE = 8;

    public BitOutputStream(OutputStream output) {
        this.output = output;
    }

    public void writeBit(int bit) throws IOException {
        if (bit < 0 || bit > 1) {
            throw new IllegalArgumentException("Illegal bit: " + bit);
        }
        digits += bit << numDigits;
        numDigits++;
        if (numDigits == BYTE_SIZE) {
            flush();
        }
    }

    /**
     * Flushes the buffer. If numDigits < BYTE_SIZE, this will
     * effectively pad the output with extra 0's, so this should
     * be called only when numDigits == BYTE_SIZE or when we are
     * closing the output.
     */
    @Override
    public void flush() throws IOException {
        output.write(digits);
        digits = 0;
        numDigits = 0;
    }

    @Override
    public void close() throws IOException {
        if (numDigits > 0) {
            flush();
        }
        output.close();
    }

    protected void finalize() throws IOException {
        close();
    }
}
