// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.container;

/**
 * Callback for code that knows how to write a specific type to a container.
 */
public interface ContainerWriterByIndex<WriteContainerT, T> {

    void writeValue(WriteContainerT app, int index, T value);
}
