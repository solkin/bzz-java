package com.tomclaw.bzz;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by solkin on 08.06.17.
 */
public class StreamHelper {

    public static void closeStream(Closeable... closeable) {
        for (Closeable c : closeable) {
            if (c != null) {
                try {
                    c.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
