// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.collections.tree.memory;

import org.talend.daikon.collections.tree.Node;
import org.talend.daikon.collections.tree.NodeLink;

public class NodeLinkMemory<K extends Comparable<K>, T> implements NodeLink<K, T> {

    private Node<K, T> node;

    @Override
    public Node<K, T> getNode() {
        return this.node;
    }

    @Override
    public void saveNode(Node<K, T> node) {
        this.node = node;
    }
}
