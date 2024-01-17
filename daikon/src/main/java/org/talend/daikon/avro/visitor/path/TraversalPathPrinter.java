// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.visitor.path;

/**
 * A printer for {@link TraversalPath}
 */
public interface TraversalPathPrinter {

    /**
     * Prints the root element
     */
    void root();

    /**
     * Appends a path element
     * 
     * @param name name of the element
     * @param position position of the element
     */
    void append(String name, int position);

    /**
     * Appends an array item
     * 
     * @param index index of the element
     */
    void arrayIndex(int index);

    /**
     * Appends a map entry
     * 
     * @param key key of the entry
     */
    void mapEntry(String key);
}
