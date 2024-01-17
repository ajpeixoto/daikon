// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PresentationItemTest {

    static public class PresItemProperties extends PropertiesImpl {

        public PresentationItem buttonValue = new PresentationItem("buttonValue");

        public PresItemProperties(String name) {
            super(name);
        }

    }

    @Test
    public void testDeserializeI18N() {
        PresItemProperties props = new PresItemProperties("test");
        props.init();
        String s = props.toSerialized();
        assertEquals("Press me", props.buttonValue.getDisplayName());
        PresItemProperties desProp = Properties.Helper.fromSerializedPersistent(s, PresItemProperties.class).object;
        assertEquals("Press me", desProp.buttonValue.getDisplayName());
    }

}
