// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.serialize.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.talend.daikon.properties.Properties;
import org.talend.daikon.serialize.SerializerDeserializer.Deserialized;

/**
 * Properties migration test after deserialization
 */
public class PropertyMigrationTest {

    private static final String propertyWithNestedPropToMigrate = "{\"@type\":\"org.talend.daikon.serialize.migration.ParentProperties\",\"versionZeroProp\":{\"@type\":\"org.talend.daikon.properties.property.StringProperty\",\"possibleValues2\":null,\"flags\":null,\"storedValue\":\"I'm born since version 0\",\"children\":{\"@type\":\"java.util.ArrayList\"},\"taggedValues\":{\"@type\":\"java.util.HashMap\"},\"size\":-1,\"occurMinTimes\":0,\"occurMaxTimes\":0,\"precision\":0,\"pattern\":null,\"nullable\":false,\"possibleValues\":null,\"currentType\":\"java.lang.String\",\"name\":\"versionZeroProp\",\"tags\":null},\"versionZeroNestedProp\":{\"__version\":1,\"versionOneProp\":{\"@type\":\"org.talend.daikon.properties.property.StringProperty\",\"possibleValues2\":null,\"flags\":null,\"storedValue\":\"I'm born since version 1\",\"children\":{\"@type\":\"java.util.ArrayList\"},\"taggedValues\":{\"@type\":\"java.util.HashMap\"},\"size\":-1,\"occurMinTimes\":0,\"occurMaxTimes\":0,\"precision\":0,\"pattern\":null,\"nullable\":false,\"possibleValues\":null,\"currentType\":\"java.lang.String\",\"name\":\"versionOneProp\",\"tags\":null},\"name\":\"versionZeroNestedProp\",\"validationResult\":null,\"tags\":null},\"name\":\"parent\",\"validationResult\":null,\"tags\":null}";

    @Test
    public void postDeserializationTest() {
        Deserialized<ParentProperties> deser = Properties.Helper.fromSerializedPersistent(propertyWithNestedPropToMigrate,
                ParentProperties.class);

        ParentProperties properties = deser.object;
        assertTrue(deser.migrated, "should be true, but not");
        assertEquals("I'm born since version 0", properties.versionZeroProp.getValue());
        assertEquals("I'm born since version 1", properties.versionZeroNestedProp.versionOneProp.getValue());

        // added properties after serialization
        assertEquals("I'm born since version 1", properties.versionOneProp.getValue());
        assertEquals("I'm born since version 2", properties.versionZeroNestedProp.versionTwoProp.getValue());
    }
}
