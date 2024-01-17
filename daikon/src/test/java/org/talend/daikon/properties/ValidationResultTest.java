// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.talend.daikon.exception.TalendRuntimeException;
import org.talend.daikon.exception.error.CommonErrorCodes;
import org.talend.daikon.properties.ValidationResult.Result;

public class ValidationResultTest {

    @Test
    public void testValidationResult() {
        assertEquals(Result.OK, new ValidationResult().getStatus());
    }

    @Test
    public void testValidationResultTalendRuntimeException() {
        TalendRuntimeException runtimeException = new TalendRuntimeException(CommonErrorCodes.UNEXPECTED_EXCEPTION);
        ValidationResult result = new ValidationResult(runtimeException);
        assertEquals(Result.ERROR, result.getStatus());
        assertEquals("UNEXPECTED_EXCEPTION", result.getMessage());
    }

    @Test
    public void testSetGetStatus() {
        assertEquals(Result.OK, new ValidationResult(Result.OK).getStatus());
        assertEquals(Result.WARNING, new ValidationResult(Result.WARNING).getStatus());
        assertEquals(Result.ERROR, new ValidationResult(Result.ERROR).getStatus());
    }

    @Test
    public void testSetGetMessage() {
        assertEquals("foo", new ValidationResult(Result.OK, "foo").getMessage());
    }

    @Test
    public void testToString() {
        assertEquals("ERROR UNEXPECTED_EXCEPTION",
                new ValidationResult(new TalendRuntimeException(CommonErrorCodes.UNEXPECTED_EXCEPTION)).toString());
    }

    @Test
    public void testValdiationResultMutable() {
        assertEquals(Result.ERROR, new ValidationResultMutable(Result.OK).setStatus(Result.ERROR).getStatus());
        assertEquals("vrm", new ValidationResultMutable(Result.OK).setMessage("vrm").getMessage());
    }

    @Test
    public void testValdiationResultMutableCopie() {
        ValidationResult vr = new ValidationResult(Result.ERROR, "error");
        assertEquals(Result.ERROR, new ValidationResultMutable(vr).getStatus());
        assertEquals("error", new ValidationResultMutable(vr).getMessage());
    }

}
