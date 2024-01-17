// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties.testproperties.nestedprop.inherited;

import static org.talend.daikon.properties.property.PropertyFactory.newProperty;

import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.testproperties.nestedprop.NestedProperties;

public class InheritedProperties extends NestedProperties {

    public static final String A_GREAT_PROP_NAME3 = "aGreatProp3"; //$NON-NLS-1$

    public Property<String> aGreatProp3 = newProperty(A_GREAT_PROP_NAME3);

    public InheritedProperties(String name) {
        super(name);
    }
}