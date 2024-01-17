// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.collections.tree.file;

import java.util.function.Supplier;

import org.talend.daikon.collections.tree.Node;
import org.talend.daikon.collections.tree.NodeBuilder;

public class BuilderFile<K extends Comparable<K>, T> implements NodeBuilder<K, T> {

    private final NodeFile<K, T> file;

    public BuilderFile(final NodeFile<K, T> file) {
        this.file = file;
    }

    @Override
    public Node<K, T> findRoot() {
        return this.file.findRoot();
    }

    @Override
    public void newRoot(final Node<K, T> root) {
        this.file.newRoot(root);
    }

    @Override
    public Node<K, T> build(K key, Supplier<T> data) {
        return this.file.createNode(key, data.get());
    }
}
