// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties;

import static org.talend.daikon.properties.property.PropertyFactory.newString;

import java.io.Serializable;
import java.text.ParseException;

import org.talend.daikon.properties.property.Property;

public class TestEmptyProperties extends PropertiesImpl implements Serializable {

    public static class InnerProperties extends PropertiesImpl {

        public Property<String> innerProperty = newString("innerProperty", "initialInnerValue");

        public InnerProperties(String name) {
            super(name);
        }

    }

    public TestEmptyProperties(String name) {
        super(name);
    }

    public InnerProperties innerProperties = new InnerProperties("innerProperties");

    public Property<String> aProperty = newString("aProperty", "initalValue");

    static public TestEmptyProperties createASetupOptionalProperties() throws ParseException {
        TestEmptyProperties properties = (TestEmptyProperties) new TestEmptyProperties("optionnalExample").init();
        return properties;
    }

}
