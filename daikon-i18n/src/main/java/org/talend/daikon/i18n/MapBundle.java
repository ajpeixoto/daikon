// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.i18n;

import java.util.Map;
import java.util.Set;

public class MapBundle extends BaseBundle {

    private final Map<String, String> values;

    public MapBundle(final Map<String, String> values) {
        this.values = values;
    }

    @Override
    protected Set<String> doGetKeys() {
        return values.keySet();
    }

    @Override
    protected Object handleGetObject(final String key) {
        return values.get(key);
    }
}
