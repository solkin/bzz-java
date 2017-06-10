package com.tomclaw.huffman.one;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
        List<TreeItem> items = new ArrayList<>(leafs);
        do {
            Collections.sort(items);
            TreeItem zero = items.remove(items.size() - 1);
            TreeItem one = items.remove(items.size() - 1);
            TreeItem item = new TreeItem(zero.getFrequency() + one.getFrequency(), zero, one);
            items.add(item);
        } while (items.size() > 1);
        return new Tree(items.get(0), leafs);
    }
}
