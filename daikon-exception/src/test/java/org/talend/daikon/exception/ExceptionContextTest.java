// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.exception;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link ExceptionContext}
 */
public class ExceptionContextTest {

    @Test
    public void createExceptionContextWithBuilder() {
        ExceptionContext context = ExceptionContext.withBuilder().put("key1", "value1").put("key2", "value2")
                .put("key3", "value3").build();
        assertTrue(context.contains("key1"));
        assertTrue(context.contains("key2"));
        assertTrue(context.contains("key3"));
    }

}
