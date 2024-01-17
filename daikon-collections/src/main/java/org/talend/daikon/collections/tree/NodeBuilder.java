// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.collections.tree;

import java.util.function.Supplier;

public interface NodeBuilder<K extends Comparable<K>, T> {

    Node<K, T> findRoot();

    void newRoot(final Node<K, T> root);

    Node<K, T> build(final K key, final Supplier<T> data);
}
