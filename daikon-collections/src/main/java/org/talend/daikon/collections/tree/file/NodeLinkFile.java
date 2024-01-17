// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.collections.tree.file;

import org.talend.daikon.collections.tree.Node;
import org.talend.daikon.collections.tree.NodeLink;

public class NodeLinkFile<K extends Comparable<K>, T> implements NodeLink<K, T> {

    private final NodeFile<K, T> file;

    private final long startPos;

    public NodeLinkFile(final NodeFile<K, T> file, long startPos) {
        super();
        this.file = file;
        this.startPos = startPos;
    }

    @Override
    public Node<K, T> getNode() {
        return this.file.getNode(this);
    }

    public long getStartPos() {
        return startPos;
    }

    @Override
    public void saveNode(final Node<K, T> node) {
        this.file.saveNode(node, this.startPos);
    }
}
