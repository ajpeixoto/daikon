// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.serialize.jsonschema;

import static org.talend.daikon.properties.presentation.Widget.widget;

import org.talend.daikon.properties.PropertiesImpl;
import org.talend.daikon.properties.PropertiesList;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;

public class ColumnProperties extends PropertiesImpl {

    private static final long serialVersionUID = 1L;

    // list of filters
    public PropertiesList<FilterRowCriteriaProperties> filters = new PropertiesList<>("filters",
            new PropertiesList.NestedPropertiesFactory<FilterRowCriteriaProperties>() {

                @Override
                public FilterRowCriteriaProperties createAndInit(String name) {
                    return (FilterRowCriteriaProperties) new FilterRowCriteriaProperties(name).init();
                }

            });

    public ColumnProperties(String name) {
        super(name);
        filters.init();
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addRow(widget(filters).setWidgetType(Widget.COLUMNS_PROPERTIES)
                .setConfigurationValue(Widget.NESTED_PROPERTIES_TYPE_OPTION, "filter"));
    }

    @Override
    public void setupProperties() {
        super.setupProperties();
        setupLayout();
        // Add a default filter criteria
        filters.createAndAddRow();
    }
}