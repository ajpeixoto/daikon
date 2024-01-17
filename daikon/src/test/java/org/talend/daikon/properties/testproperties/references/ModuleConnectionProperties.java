// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties.testproperties.references;

import org.talend.daikon.properties.PropertiesImpl;
import org.talend.daikon.properties.presentation.Form;

public class ModuleConnectionProperties extends PropertiesImpl {

    public ConnectionProperties connection = new ConnectionProperties("connection");

    public ModuleConnectionProperties(String name) {
        super(name);
    }

    @Override
    public void setupLayout() {
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addRow(connection.getForm(Form.MAIN));
    }

}