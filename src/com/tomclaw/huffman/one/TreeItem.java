package com.tomclaw.huffman.one;

/**
 * Created by solkin on 08.06.17.
 */
class TreeItem implements Comparable<TreeItem> {

    private final int value; // byte, 0 - 255
    private final int frequency;
    private final TreeItem zero;
    private final TreeItem one;
    private int bit = 0;
    private TreeItem parent = null;

    public TreeItem(int value, int frequency) {
        this(value, frequency, null, null);
    }

    public TreeItem(int frequency, TreeItem zero, TreeItem one) {
        this((byte) 0, frequency, zero, one);
    }

    public TreeItem(int value, int frequency, TreeItem zero, TreeItem one) {
        this.value = value;
        this.frequency = frequency;
        this.zero = zero;
        this.one = one;
        if (zero != null) {
            zero.setBit(0);
            zero.setParent(this);
        }
        if (one != null) {
            one.setBit(1);
            one.setParent(this);
        }
    }

    public int getValue() {
        return value;
    }

    public int getFrequency() {
        return frequency;
    }

    public boolean hasChild() {
        return zero != null && one != null;
    }

    public TreeItem getChild(int bit) {
        if (bit == 1) {
            return one;
        } else {
            return zero;
        }
    }

    private void setParent(TreeItem parent) {
        this.parent = parent;
    }

    public TreeItem getParent() {
        return parent;
    }

    private void setBit(int bit) {
        this.bit = bit;
    }

    public int getBit() {
        return bit;
    }

    @Override
    public int compareTo(TreeItem o) {
        return Integer.compare(o.frequency, frequency);
    }

    @Override
    public String toString() {
        return "TreeItem{" +
                "value=" + value +
                ", frequency=" + frequency +
                ", zero=" + zero +
                ", one=" + one +
                '}';
    }
}
