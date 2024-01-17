// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.talend.daikon.definition.Definition;
import org.talend.daikon.definition.service.DefinitionRegistryService;
import org.talend.daikon.properties.test.AbstractPropertiesTest;
import org.talend.daikon.properties.testproperties.TestProperties;

public class TestAbstractPropertiesTest {

    @Test
    public void test() {
        AbstractPropertiesTest propertiesTest = new AbstractPropertiesTest() {

            @Override
            public DefinitionRegistryService getDefinitionRegistry() {
                DefinitionRegistryService defRegServ = mock(DefinitionRegistryService.class);
                Definition repDef = when(mock(Definition.class).getName()).thenReturn("NAME").getMock();
                when(repDef.getPropertiesClass()).thenReturn(TestProperties.class);
                when(defRegServ.getDefinitionsMapByType(Definition.class)).thenReturn(Collections.singletonMap("NAME", repDef));
                return defRegServ;
            }
        };
        // check for an existing definition
        propertiesTest.assertComponentIsRegistered("NAME");
        // check for a non existing def
        try {
            propertiesTest.assertComponentIsRegistered("XXX");
            fail("assertiong should have failed in the above line");
        } catch (AssertionError ae) {
            // this is ok if we have an error
        }

    }

}
