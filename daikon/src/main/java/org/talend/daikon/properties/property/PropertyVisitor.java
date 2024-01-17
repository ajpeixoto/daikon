// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties.property;

import org.talend.daikon.properties.AnyPropertyVisitor;
import org.talend.daikon.properties.Properties;

/**
 * Provides a default {@link AnyPropertyVisitor} implementation which can be used for a subclass that is interested only in
 * visiting {@link Property} objects.
 */
public abstract class PropertyVisitor implements AnyPropertyVisitor {

    @Override
    public void visit(Properties properties, Properties parent) {
        // nothing to done here
    }
}
