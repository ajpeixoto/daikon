// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.collections.tree.file;

/**
 * To serialize / deserialize data.
 * 
 * @param <T> : class to serialize.
 */
public interface Serializer<T> {

    byte[] serialize(final T object);

    T deserialize(final byte[] data);
}
