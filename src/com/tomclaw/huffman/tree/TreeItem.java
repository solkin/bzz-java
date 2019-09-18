package com.tomclaw.huffman.tree;

/**
 * Created by solkin on 08.06.17.
 */
class TreeItem implements Comparable<TreeItem> {

    private int value; // byte, 0 - 255
    private int frequency;
    private TreeItem zero;
    private TreeItem one;
    private int bit = 0;
    private TreeItem parent = null;

    public TreeItem() {
        this(0, 0, null, null);
    }

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

    public void setValue(int value) {
        this.value = value;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void setChild(int bit, TreeItem child) {
        if (bit == 1) {
            one = child;
        } else {
            zero = child;
        }
    }

    @Override
    public int compareTo(TreeItem o) {
        if (o.frequency != frequency) {
            return Integer.compare(o.frequency, frequency);
        } else {
            return Integer.compare(o.value, value);
        }
    }

    @Override
    public String toString() {
        return "TreeItem{" +
                "value=" + value +
                ", frequency=" + frequency +
                ", zero=" + zero +
                ", tree=" + one +
                '}';
    }
}
