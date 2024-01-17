// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com

// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.serialize;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.apache.avro.SchemaBuilder;
import org.talend.daikon.NamedThing;
import org.talend.daikon.avro.SchemaConstants;
import org.talend.daikon.properties.Properties;
import org.talend.daikon.properties.property.Property;

public class FullExampleTestUtil {

    static public FullExampleProperties createASetupFullExampleProperties() throws ParseException {
        FullExampleProperties properties = (FullExampleProperties) new FullExampleProperties("fullexample").init();
        properties.stringProp.setValue("abc");
        properties.integerProp.setValue(1);
        properties.longProp.setValue(100l);
        properties.hideStringPropProp.setValue(false);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        properties.dateProp.setValue(df.parse("2016-10-05T01:23:45.000Z"));
        properties.schema.setValue(
                SchemaBuilder.builder().record("test").prop(SchemaConstants.INCLUDE_ALL_FIELDS, "true").fields().endRecord());

        properties.commonProp.colEnum.setValue(FullExampleProperties.CommonProperties.ColEnum.FOO);
        properties.commonProp.colBoolean.setValue(true);
        properties.commonProp.colString.setValue("common_abc");

        properties.tableProp.colListBoolean.setValue(Arrays.asList(new Boolean[] { true, false, true }));
        properties.tableProp.colListEnum.setValue(Arrays
                .asList(new FullExampleProperties.TableProperties.ColEnum[] { FullExampleProperties.TableProperties.ColEnum.FOO,
                        FullExampleProperties.TableProperties.ColEnum.BAR, FullExampleProperties.TableProperties.ColEnum.FOO }));
        properties.tableProp.colListString.setValue(Arrays.asList(new String[] { "a", "b", "c" }));
        return properties;
    }

    static public void assertPropertiesValueAreEquals(Properties expectedProperties, Properties actualProperties) {
        List<NamedThing> expectedProps = expectedProperties.getProperties();
        for (NamedThing expectedProp : expectedProps) {
            String propName = expectedProp.getName();
            NamedThing actualProp = actualProperties.getProperty(propName);
            assertNotNull(actualProp, propName);
            if (expectedProp instanceof Properties) {
                assertTrue(actualProp instanceof Properties, propName + " should Properties");
                assertPropertiesValueAreEquals((Properties) expectedProp, (Properties) actualProp);
            } else if (expectedProp instanceof Property) {
                assertTrue(actualProp instanceof Property, propName + " should Property");
                assertEquals(((Property) expectedProp).getValue(), ((Property) actualProp).getValue(), propName);
            }
        }

    }
}
