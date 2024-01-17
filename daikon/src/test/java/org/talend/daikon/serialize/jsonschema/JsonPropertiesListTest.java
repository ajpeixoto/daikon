// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.serialize.jsonschema;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.talend.daikon.properties.PropertiesList;
import org.talend.daikon.properties.TestPropertiesList.TestComponentProperties;
import org.talend.daikon.properties.TestPropertiesList.TestEnum;
import org.talend.daikon.properties.TestPropertiesList.TestProperties;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.serialize.FullExampleTestUtil;

/**
 * created by dmytro.chmyga on Jul 13, 2017
 */
public class JsonPropertiesListTest {

    @Test
    public void testResolveJsonPropertiesList() throws Exception {
        String jsonDataStr = JsonSchemaUtilTest.readJson("PropertiesListProperties.json");
        TestComponentProperties properties = (TestComponentProperties) JsonSchemaUtil.fromJson(jsonDataStr,
                new TestComponentProperties("compProperties").init());

        FullExampleTestUtil.assertPropertiesValueAreEquals(createAndSetupTestProperties(), properties);
    }

    @Test
    public void testGenerateJsonPropertiesList() throws URISyntaxException, IOException {
        String expectedJson = JsonSchemaUtilTest.readJson("PropertiesListProperties.json");
        TestComponentProperties compProperties = createAndSetupTestProperties();
        String jsonValue = JsonSchemaUtil.toJson(compProperties, Form.MAIN, "someDef");
        assertEquals(expectedJson, jsonValue);
    }

    private static TestComponentProperties createAndSetupTestProperties() {
        TestComponentProperties compProperties = new TestComponentProperties("compProperties");
        PropertiesList<TestProperties> propsList = compProperties.filters;
        compProperties.init();
        TestProperties testProps = new TestProperties("testProps");
        testProps.init();
        testProps.intProp.setValue(1);
        testProps.stringProp.setValue(TestEnum.ONE);
        TestProperties testProps1 = new TestProperties("testProps1");
        testProps1.init();
        testProps1.intProp.setValue(2);
        testProps1.stringProp.setValue(TestEnum.TWO);
        propsList.addRow(testProps);
        propsList.addRow(testProps1);
        propsList.refreshLayout(propsList.getForm(Form.MAIN));
        return compProperties;
    }

}
