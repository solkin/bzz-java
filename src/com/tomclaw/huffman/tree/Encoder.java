package com.tomclaw.huffman.tree;

import com.tomclaw.huffman.BitOutputStream;

import java.io.*;
import java.util.*;

import static com.tomclaw.huffman.StreamHelper.closeStream;

/**
 * Created by ivsolkin on 08.06.17.
 */
public class Encoder {

    private static final int VERSION_1 = 1;
    private static final int VERSION_2 = 2;

    private final File inputFile;
    private final File outputFile;

    private static final int BUFFER_SIZE = 10 * 1024;
    private byte[] buffer = new byte[BUFFER_SIZE];

    public Encoder(File inputFile, File outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public void compress() throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(inputFile);
            Tree tree = scan(inputStream);
            inputStream = new FileInputStream(inputFile);
            outputStream = new FileOutputStream(outputFile);
            long fileSize = inputFile.length();
            long time = System.currentTimeMillis();
            encode(tree, inputStream, outputStream, fileSize);
            System.out.println("compress: " + (System.currentTimeMillis() - time) + " ms.");
        } finally {
            closeStream(inputStream, outputStream);
        }
    }

    private void writeDictionary(int[][] dictionary, int count, DataOutputStream dataStream) throws IOException {
        dataStream.writeShort(VERSION_2);
        dataStream.writeByte(count);
        for (int c = 0; c < dictionary.length; c++) {
            if (dictionary[c] != null && dictionary[c].length > 0) {
                dataStream.writeByte(c);
                dataStream.writeByte(dictionary[c].length);
            }
        }
        ByteArrayOutputStream baos = null;
        BitOutputStream bitStream = null;
        try {
            baos = new ByteArrayOutputStream();
            bitStream = new BitOutputStream(baos);
            for (int[] path : dictionary) {
                if (path != null) {
                    for (int value : path) {
                        bitStream.writeBit(value);
                    }
                }
            }
            bitStream.flush();
            dataStream.write(baos.toByteArray());
        } finally {
            closeStream(bitStream);
            closeStream(baos);
        }
    }

    private void writeFileSize(long fileSize, DataOutputStream dataStream) throws IOException {
        dataStream.writeLong(fileSize);
        dataStream.flush();
    }

    private void encode(Tree tree, InputStream inputStream, OutputStream outputStream, long fileSize) throws IOException {
        DataOutputStream dataStream = null;
        BitOutputStream bitStream = null;
        BufferedOutputStream bufferedStream = null;
        try {
            int[][] dictionary = flatTree(tree);

            dataStream = new DataOutputStream(outputStream);
            writeDictionary(dictionary, tree.getLeafs().size(), dataStream);

            writeFileSize(fileSize, dataStream);

            bufferedStream = new BufferedOutputStream(outputStream, BUFFER_SIZE);
            bitStream = new BitOutputStream(bufferedStream);

            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                for (int c = read; c > 0; c--) {
                    int b = buffer[read - c] - Byte.MIN_VALUE;
                    int[] path = dictionary[b];
                    for (int i = path.length - 1; i >= 0; i--) {
                        bitStream.writeBit(path[i]);
                    }
                }
            }
            bitStream.flush();
            bufferedStream.flush();
        } finally {
            closeStream(bitStream, dataStream, inputStream, bufferedStream);
        }
    }

    private Tree scan(InputStream inputStream) throws IOException {
        try {
            int[] bytes = new int[Byte.MAX_VALUE - Byte.MIN_VALUE + 1];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                for (int c = 0; c < read; c++) {
                    int b = buffer[c] - Byte.MIN_VALUE;
                    bytes[b]++;
                }
            }

            List<TreeItem> items = new LinkedList<>();
            for (int c = 0; c < bytes.length; c++) {
                int frequency = bytes[c];
                if (frequency > 0) {
                    items.add(new TreeItem(c, frequency));
                }
            }

            return Tree.create(items);
        } finally {
            closeStream(inputStream);
        }
    }

    private int[][] flatTree(Tree tree) {
        Collection<TreeItem> leafs = tree.getLeafs();
        TreeItem rootItem = tree.getRootItem();

        int[][] dictionary = new int[Byte.MAX_VALUE - Byte.MIN_VALUE + 1][];
        for (TreeItem item : leafs) {

            int length = 0;
            TreeItem parent = item;
            do {
                length++;
            } while ((parent = parent.getParent()) != rootItem);

            int[] path = new int[length];
            int c = 0;
            parent = item;
            do {
                path[c++] = parent.getBit();
            } while ((parent = parent.getParent()) != rootItem);

            dictionary[item.getValue()] = path;
        }
        return dictionary;
    }
}
