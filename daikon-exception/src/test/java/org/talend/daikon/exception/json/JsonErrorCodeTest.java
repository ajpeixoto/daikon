// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.exception.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;
import org.talend.daikon.exception.ExceptionContext;
import org.talend.daikon.exception.TalendRuntimeException;
import org.talend.daikon.exception.error.CommonErrorCodes;
import org.talend.daikon.exception.error.ErrorCode;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonErrorCodeTest {

    @Test
    public void testDeserialize() throws IOException {
        // check that the CodeError serialization can be deserialized into an JsonCodeError
        TalendRuntimeException talendRuntimeException = new TalendRuntimeException(CommonErrorCodes.MISSING_I18N_TRANSLATOR,
                ExceptionContext.build().put("key", "the key").put("baseName", "the baseName"));
        StringWriter writer = new StringWriter();
        talendRuntimeException.writeTo(writer);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonErrorCode deserializedCode = objectMapper.reader(JsonErrorCode.class).readValue(writer.toString());
        ErrorCode expectedCode = talendRuntimeException.getCode();
        assertEquals(expectedCode.getCode(), deserializedCode.getCode());
        assertEquals(expectedCode.getGroup(), deserializedCode.getGroup());
        assertEquals(expectedCode.getProduct(), deserializedCode.getProduct());
        assertThat(expectedCode.getExpectedContextEntries(),
                containsInAnyOrder(deserializedCode.getExpectedContextEntries().toArray()));
        assertThat(talendRuntimeException.getContext().entries(),
                containsInAnyOrder(deserializedCode.getContext().entrySet().toArray()));
    }

}
