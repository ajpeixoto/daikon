// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties.testproperties.references;

import org.talend.daikon.properties.PropertiesImpl;
import org.talend.daikon.properties.presentation.Form;

public class MultipleRefProperties extends PropertiesImpl {

    public ConnectionProperties connection = new ConnectionProperties("connection");

    public ModuleConnectionProperties module = new ModuleConnectionProperties("module");

    public MultipleRefProperties(String name) {
        super(name);
    }

    @Override
    public void setupProperties() {
        super.setupProperties();
        module.connection = connection;
    }

    @Override
    public void setupLayout() {
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addRow(connection.getForm(Form.MAIN));
        mainForm.addRow(module.getForm(Form.MAIN));
    }

}