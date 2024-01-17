// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties.testproperties.nestedprop;

import static org.talend.daikon.properties.property.PropertyFactory.newBoolean;
import static org.talend.daikon.properties.property.PropertyFactory.newProperty;
import static org.talend.daikon.properties.property.PropertyFactory.newString;

import org.talend.daikon.properties.PropertiesImpl;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.property.Property;

public class NestedProperties extends PropertiesImpl {

    public static final String A_GREAT_PROP_NAME = "aGreatProperty"; //$NON-NLS-1$

    public Property<String> aGreatProperty = newProperty(A_GREAT_PROP_NAME);

    public Property<String> anotherProp = newString("anotherProp");

    public Property<Boolean> booleanProp = newBoolean("booleanProp");

    public NestedNestedProperties nestedProp = new NestedNestedProperties("nestedProp");

    public NestedProperties(String name) {
        super(name);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form form = Form.create(this, Form.MAIN);
        form.addRow(aGreatProperty);
        form.addRow(anotherProp);
    }

    @Override
    public void refreshLayout(Form form) {
        // change visibility according to anotherProp value
        form.getWidget(anotherProp.getName()).setHidden(booleanProp.getValue());
    }
}