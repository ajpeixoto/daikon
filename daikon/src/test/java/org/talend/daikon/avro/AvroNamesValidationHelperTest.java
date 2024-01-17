// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link org.talend.daikon.avro.AvroNamesValidationHelper}
 */
public class AvroNamesValidationHelperTest {

    private static final Set<String> AVRO_INVALID_NAMES = new HashSet<String>(Arrays.asList("abstract", "const", "continue",
            "float", "for", "native", "new", "super", "switch", "while", "true", "false", "null"));

    private static final Set<String> AVRO_COMPATIBLE_NAMES = new HashSet<String>(Arrays.asList("_abstract", "_const", "_continue",
            "_float", "_for", "_native", "_new", "_super", "_switch", "_while", "_true", "_false", "_null"));

    @Test
    public void testGetAvroCompatibleName() {
        Set<String> convertedNames = new HashSet<String>();

        for (String name : AVRO_INVALID_NAMES) {
            convertedNames.add(AvroNamesValidationHelper.getAvroCompatibleName(name));
        }

        assertEquals(convertedNames, AVRO_COMPATIBLE_NAMES);
    }
}
