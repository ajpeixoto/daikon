// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.container;

/**
 * Callback for code that knows how to read a specific type from a container.
 */
public interface ContainerReaderByIndex<ReadContainerT, T> {

    T readValue(ReadContainerT obj, int index);
}
