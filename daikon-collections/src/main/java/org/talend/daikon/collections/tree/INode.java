// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.collections.tree;

/**
 * Node of AvlTree
 * 
 * @param <K> : Class for Key.
 * @param <T> : Class for stored data.
 */
public interface INode<K extends Comparable<K>, T> {

    K getKey();

    T getData();

    INode<K, T> get(final K key);

    INode<K, T> getChild(int num);
}
