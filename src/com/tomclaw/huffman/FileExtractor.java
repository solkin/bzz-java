package com.tomclaw.huffman;

import com.tomclaw.huffman.tree.Decoder;

import java.io.File;
import java.io.IOException;

/**
 * Created by solkin on 09.06.17.
 */
public class FileExtractor {

    public void start(String inputPath, String outputPath) throws Throwable {
        File input = new File(inputPath);
        File output = new File(outputPath);
        long time = System.currentTimeMillis();
        extractVersionOne(input, output);
        time = System.currentTimeMillis() - time;
        int compression = (int) (100L * input.length() / output.length());
        float speed = (1000 * output.length() / time) / 1024F;
        System.out.println(String.format("%d ms, %.2f KiB/sec, %d%%", time, speed, compression));
    }

    private void extractVersionOne(File input, File output) {
        Decoder decoder = new Decoder(input, output);
        try {
            decoder.extract();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

