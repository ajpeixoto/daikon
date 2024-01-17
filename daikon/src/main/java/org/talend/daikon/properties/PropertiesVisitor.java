// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties;

import org.talend.daikon.properties.property.Property;

/**
 * Provides a default Properties visitor implementation for visiting only Properties typed values
 */
public abstract class PropertiesVisitor implements AnyPropertyVisitor {

    @Override
    public void visit(Property property, Properties parent) {
        // nothing to done here

    }

}
