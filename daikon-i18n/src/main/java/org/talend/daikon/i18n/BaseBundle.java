// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.i18n;

import static java.util.Collections.enumeration;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Set;

public abstract class BaseBundle extends ResourceBundle {

    protected abstract Set<String> doGetKeys();

    @Override
    protected Set<String> handleKeySet() {
        return doGetKeys();
    }

    @Override
    public Enumeration<String> getKeys() {
        return enumeration(doGetKeys());
    }
}
