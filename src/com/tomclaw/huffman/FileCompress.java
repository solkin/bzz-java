package com.tomclaw.huffman;

import com.tomclaw.huffman.tree.Encoder;

import java.io.File;
import java.io.IOException;

/**
 * Created by ivsolkin on 08.06.17.
 */
public class FileCompress {

    public void start(String inputPath, String outputPath) throws Throwable {
        File input = new File(inputPath);
        File output = new File(outputPath);
        long time = System.currentTimeMillis();
        compressVersionOne(input, output);
        time = System.currentTimeMillis() - time;
        int compression = (int) (100L * output.length() / input.length());
        float speed = (1000 * input.length() / time) / 1024F;
        System.out.println(String.format("%d ms, %.2f KiB/sec, %d%%", time, speed, compression));
    }

    private void compressVersionOne(File input, File output) {
        Encoder encoder = new Encoder(input, output);
        try {
            encoder.compress();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
