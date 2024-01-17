// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.collections.tree;

public interface NodeLink<K extends Comparable<K>, T> {

    Node<K, T> getNode();

    void saveNode(final Node<K, T> node);

}
