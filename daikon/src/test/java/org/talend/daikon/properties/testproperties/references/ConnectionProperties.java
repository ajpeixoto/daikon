// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties.testproperties.references;

import org.talend.daikon.properties.PresentationItem;
import org.talend.daikon.properties.PropertiesImpl;
import org.talend.daikon.properties.presentation.Form;

public class ConnectionProperties extends PropertiesImpl {

    public PresentationItem presItem = new PresentationItem("presItem");

    public ConnectionProperties(String name) {
        super(name);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form form = Form.create(this, Form.MAIN);
        form.addRow(presItem);
    }

}