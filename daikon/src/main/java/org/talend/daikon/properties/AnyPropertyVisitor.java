// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties;

import org.talend.daikon.properties.property.Property;

/**
 * Visit all members of a {@link Properties} object except {@link PresentationItem}
 */
public interface AnyPropertyVisitor {

    /**
     * Visit the Property.
     * 
     * @param property, the visited property
     * @param parent, the parent of the enclosing {@link Properties} object, or null if none.
     */
    void visit(Property property, Properties parent);

    /**
     * Visit the Properties
     * 
     * @param properties, the visited properties object
     * @param parent, the properties parent or null if none.
     */
    void visit(Properties properties, Properties parent);
}
