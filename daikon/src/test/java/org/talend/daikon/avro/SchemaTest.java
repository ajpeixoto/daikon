// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.junit.jupiter.api.Test;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

import com.cedarsoftware.util.io.JsonWriter;

public class SchemaTest {

    @Test
    public void testSchemaSerialized() throws Throwable {
        Schema main = SchemaBuilder.record("Main").fields().name("C").type().stringType().noDefault().name("D").type()
                .stringType().noDefault().endRecord();

        Property schemaMain = PropertyFactory.newSchema("main");
        schemaMain.setValue(main);

        JsonWriter.objectToJson(schemaMain);
    }

}
