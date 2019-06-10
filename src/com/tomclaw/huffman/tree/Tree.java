package com.tomclaw.huffman.tree;

import java.util.*;

/**
 * Created by solkin on 08.06.17.
 */
public class Tree {

    private final TreeItem rootItem;
    private final Collection<TreeItem> leafs;

    private Tree(TreeItem rootItem, Collection<TreeItem> leafs) {
        this.rootItem = rootItem;
        this.leafs = leafs;
    }

    public TreeItem getRootItem() {
        return rootItem;
    }

    public Collection<TreeItem> getLeafs() {
        return leafs;
    }

    public static Tree create(Collection<TreeItem> leafs) {
        LinkedList<TreeItem> items = new LinkedList<>(leafs);
        do {
            Collections.sort(items);
            TreeItem zero = items.removeLast();
            TreeItem one = items.removeLast();
            TreeItem item = new TreeItem(zero.getFrequency() + one.getFrequency(), zero, one);
            items.add(item);
        } while (items.size() > 1);
        return new Tree(items.getFirst(), leafs);
    }
}
