// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.java.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.talend.daikon.exception.TalendRuntimeException;
import org.talend.daikon.exception.error.CommonErrorCodes;

public class ClosableLRUMap<K, V> extends LinkedHashMap<K, V> {

    protected final int _maxEntries;

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        boolean removeOldest = size() > _maxEntries;
        if (removeOldest) {
            closeValue(eldest.getValue());
        }
        return removeOldest;
    }

    public ClosableLRUMap(int initialEntries, int maxEntries) {
        super(initialEntries, 0.8f, true);
        _maxEntries = maxEntries;
    }

    @Override
    public V remove(Object key) {
        V value = get(key);
        closeValue(value);
        return super.remove(key);
    }

    @Override
    public boolean remove(Object key, Object value) {
        closeValue(value);
        return super.remove(key, value);
    }

    @Override
    public void clear() {
        for (V value : values()) {
            closeValue(value);
        }
        super.clear();
    }

    private void closeValue(Object value) {
        if (value instanceof AutoCloseable) {
            try {
                ((AutoCloseable) value).close();
            } catch (Exception e) {
                new TalendRuntimeException(CommonErrorCodes.UNEXPECTED_EXCEPTION, e);
            }
        }
    }
}
