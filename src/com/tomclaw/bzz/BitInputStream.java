package com.tomclaw.bzz;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ivsolkin on 08.06.17.
 */
public class BitInputStream implements Closeable {

    private final InputStream input;
    private int digits;
    private int numDigits;
    private boolean isFirstByteRead;

    private static final int BYTE_SIZE = 8;

    public BitInputStream(InputStream input) {
        this.input = input;
        isFirstByteRead = false;
    }

    public int readBit() throws IOException {
        if (!isFirstByteRead) {
            nextByte();
        }
        if (digits == -1) {
            return -1;
        }
        int result = digits % 2;
        digits /= 2;
        numDigits++;
        if (numDigits == BYTE_SIZE) {
            nextByte();
        }
        return result;
    }

    private void nextByte() throws IOException {
        digits = input.read();
        numDigits = 0;
        isFirstByteRead = true;
    }

    @Override
    public void close() throws IOException {
        input.close();
    }

}
