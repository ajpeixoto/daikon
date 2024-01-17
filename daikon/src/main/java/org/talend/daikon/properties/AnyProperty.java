// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties;

import java.io.Serializable;

import org.talend.daikon.NamedThing;
import org.talend.daikon.properties.property.Property;

/**
 * A common interface for the members of a {@link Properties} object.
 *
 * This is implemented by {@link Property} and {@link Properties} and is used to allow the members of the {@code Properties}
 * object to be visited.
 */
public interface AnyProperty extends NamedThing, Serializable {

    /**
     * Be visited from it's parent.
     * 
     * @param visitor the visitor of the object
     * @param parent Properties that issued the visit or null if no parent is set.
     * 
     */
    void accept(AnyPropertyVisitor visitor, Properties parent);
}
