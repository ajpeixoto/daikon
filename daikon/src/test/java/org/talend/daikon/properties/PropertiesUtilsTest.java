// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

public class PropertiesUtilsTest {

    private static class InjectToProperties extends PropertiesImpl {

        @Inject
        public Object obj;

        public InjectToProperties(String name) {
            super(name);
        }

    }

    @Test
    public void injectObjectTest() {
        InjectToProperties props = new InjectToProperties("props");
        Object injectedObject = new Object();

        PropertiesUtils.injectObject(props, injectedObject);

        assertSame(injectedObject, props.obj, "Injected object is not the same");
    }

}
