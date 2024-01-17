// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.serialize.jsonschema;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.talend.daikon.serialize.FullExampleProperties;
import org.talend.daikon.serialize.FullExampleTestUtil;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonDataGeneratorTest {

    @Test
    public void genData() throws Exception {
        String jsonStr = JsonSchemaUtilTest.readJson("FullExampleJsonData.json");
        FullExampleProperties properties = FullExampleTestUtil.createASetupFullExampleProperties();

        JsonDataGenerator generator = new JsonDataGenerator();
        ObjectNode json = generator.genData(properties, null, "def1");
        assertEquals(jsonStr, json.toString());
        assertThat(json.get("stringWithPlaceholder"), nullValue());

        // Modify the placeholder value
        properties.stringWithPlaceholder.setValue("not empty");
        json = generator.genData(properties, null, "def1");
        assertThat(json.get("stringWithPlaceholder").asText(), is("not empty"));
    }
}
