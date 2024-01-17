// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.collections.tree.memory;

import java.util.function.Supplier;

import org.talend.daikon.collections.tree.Node;
import org.talend.daikon.collections.tree.NodeBuilder;
import org.talend.daikon.collections.tree.NodeLink;

public class BuilderMemo<K extends Comparable<K>, T> implements NodeBuilder<K, T> {

    private Node<K, T> root = null;

    @Override
    public Node<K, T> findRoot() {
        return this.root;
    }

    @Override
    public void newRoot(final Node<K, T> root) {
        this.root = root;
    }

    @Override
    public Node<K, T> build(K key, Supplier<T> data) {
        final NodeLink<K, T> link = new NodeLinkMemory<>();
        final Node<K, T> node = new Node<>(link, key, data);
        link.saveNode(node);
        return node;
    }

}
