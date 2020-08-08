package com.tomclaw.bzz;

import com.tomclaw.bzz.tree.Encoder;

import java.io.File;
import java.io.IOException;

/**
 * Created by ivsolkin on 08.06.17.
 */
public class FileCompress {

    public void start(String inputPath, String outputPath) {
        File input = new File(inputPath);
        File output = new File(outputPath);
        long time = System.currentTimeMillis();
        compressVersionOne(input, output);
        time = System.currentTimeMillis() - time;
        int compression = (int) (100L * output.length() / input.length());
        float speed = (1000F * input.length() / time) / 1024F;
        System.out.printf("%d ms, %.2f KiB/sec, %d%%\n", time, speed, compression);
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
