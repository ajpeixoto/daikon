// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.schema;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.apache.avro.Schema;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DataAvroSchemaTest {

    @ParameterizedTest
    @ValueSource(strings = { "/dataset_valid.json", "/dataset_valid2.json", "/dataset_datetime.json" })
    public void givenInput_whenParsingAsAvroSchema_thenValid(String jsonFile) throws IOException {

        Schema schema = new Schema.Parser().parse(DataAvroSchemaTest.class.getResourceAsStream(jsonFile));
        assertNotNull(schema);
    }
}
