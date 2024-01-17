// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties.testproperties;

import static org.talend.daikon.properties.property.PropertyFactory.newProperty;

import org.talend.daikon.i18n.GlobalI18N;
import org.talend.daikon.i18n.I18nMessages;
import org.talend.daikon.properties.PropertiesImpl;
import org.talend.daikon.properties.property.Property;

public class PropertiesWithDefinedI18N extends PropertiesImpl {

    public static final String A_GREAT_PROP_NAME2 = "aGreatProp2"; //$NON-NLS-1$

    public Property<String> aGreatProp2 = newProperty(A_GREAT_PROP_NAME2);

    public PropertiesWithDefinedI18N(String name) {
        super(name);
    }

    /**
     * This uses the globalContext static variable and the current Class location to find the resource bundle names
     * messages
     * 
     * @return the already set I18nMessages or a newly created one base on the current Class package.
     */
    @Override
    protected I18nMessages createI18nMessageFormater() {
        return GlobalI18N.getI18nMessageProvider().getI18nMessages(this.getClass().getClassLoader(),
                "org/talend/daikon/properties/testproperties/specificmessages");
    }

}