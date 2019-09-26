package com.tomclaw.bzz.tree;

import com.tomclaw.bzz.BitInputStream;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static com.tomclaw.bzz.StreamHelper.closeStream;

/**
 * Created by solkin on 08.06.17.
 */
public class Decoder {

    private static final int VERSION_1 = 1;
    private static final int VERSION_2 = 2;

    private final File inputFile;
    private final File outputFile;

    private static final int BUFFER_SIZE = 10 * 1024;

    public Decoder(File inputFile, File outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public void extract() throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(inputFile), BUFFER_SIZE);
            outputStream = new BufferedOutputStream(new FileOutputStream(outputFile), BUFFER_SIZE);
            decode(inputStream, outputStream);
        } finally {
            closeStream(inputStream, outputStream);
        }
    }

    private void decode(InputStream inputStream, OutputStream outputStream) throws IOException {
        DataInputStream dataInput = null;
        BitInputStream bitStream = null;
        try {
            dataInput = new DataInputStream(inputStream);
            bitStream = new BitInputStream(dataInput);
            Tree tree = readDictionary(dataInput);
            long fileSize = readFileSize(dataInput);
            TreeItem rootItem = tree.getRootItem();
            TreeItem item = rootItem;
            int read;
            long written = 0;
            while ((read = bitStream.readBit()) != -1) {
                TreeItem child = item.getChild(read);
                if (child != null) {
                    if (child.hasChild()) {
                        item = child;
                    } else {
                        outputStream.write((byte) child.getValue());
                        if (++written == fileSize) {
                            break;
                        }
                        item = rootItem;
                    }
                }
            }
        } finally {
            closeStream(bitStream, dataInput);
        }
    }

    private Tree readDictionary(DataInputStream dataStream) throws IOException {
        int version = dataStream.readShort();
        int leafsCount = dataStream.readInt();
        switch (version) {
            case VERSION_1:
                List<TreeItem> leafs = new LinkedList<>();
                for (int c = 0; c < leafsCount; c++) {
                    int value = Byte.toUnsignedInt(dataStream.readByte());
                    int frequency = dataStream.readInt();
                    leafs.add(new TreeItem(value, frequency));
                }
                return Tree.create(leafs);
            case VERSION_2:
                BitInputStream bitStream = new BitInputStream(dataStream);
                int[] sizes = new int[256];
                for (int c = 0; c < leafsCount; c++) {
                    int value = Byte.toUnsignedInt(dataStream.readByte());
                    int size = Byte.toUnsignedInt(dataStream.readByte());
                    sizes[value] = size;
                }
                TreeItem root = new TreeItem();
                TreeItem parent = root;
                for (int i = 0; i < sizes.length; i++) {
                    int size = sizes[i];
                    if (size > 0) {
                        for (int c = 0; c < size; c++) {
                            int bit = bitStream.readBit();
                            TreeItem item = parent.getChild(bit);
                            if (item == null) {
                                item = new TreeItem();
                                parent.setChild(bit, item);
                            }
                            parent = item;
                        }
                        parent.setValue(i);
                        parent = root;
                    }
                }
                return Tree.create(root);
            default:
                throw new UnsupportedEncodingException("Version " + version + " is not supported!");
        }
    }

    private long readFileSize(DataInputStream dataStream) throws IOException {
        return dataStream.readLong();
    }
}
