// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.talend.daikon.properties.ValidationResult.Result;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;
import org.talend.daikon.properties.validation.ValidationHelper;
import org.talend.daikon.properties.validation.Validator;

/**
 * Created by dmytro.sylaiev on 5/29/2017.
 */
public class ValidationResultsTest {

    public static final class ValidationProperties extends PropertiesImpl {

        public NestedValidationProperties nestedProperties = new NestedValidationProperties("nestedProperties");

        public ValidationProperties(String name) {
            super(name);
        }

        public ValidationResult afterNestedProperties() {
            return ValidationResult.OK;
        }

        public ValidationResult validateNestedProperties() {
            return nestedProperties.afterPropertyToValidate();
        }

    }

    public static final class NestedValidationProperties extends PropertiesImpl {

        public Property<String> propertyToValidate = PropertyFactory.newString("propertyToValidate");

        public NestedValidationProperties(String name) {
            super(name);
        }

        @Override
        public void setupProperties() {
            super.setupProperties();

            propertyToValidate.setValidator(new EmptyStringValidator());
        }

        public ValidationResult afterPropertyToValidate() {
            return propertyToValidate.validate();
        }

    }

    public static class EmptyStringValidator implements Validator<String> {

        @Override
        public ValidationResult validate(String value) {
            ValidationResult result = ValidationResult.OK;
            if (value == null || value.isEmpty()) {
                result = new ValidationResult(Result.ERROR, "Empty string");
            }
            return result;
        }

    }

    @Test
    public void testOkValidationResults() {
        ValidationResults validationResults = createDefaultValidationResults();

        Map<String, ValidationResult> allResults = validationResults.getAll();
        assertEquals(allResults.size(), 2);
        ValidationResult testResult = allResults.get("testResult");
        assertEquals(Result.OK, testResult.getStatus());
        assertEquals("OK", testResult.getMessage());
    }

    @Test
    public void testErrorValidationResults() {
        ValidationResults validationResults = createDefaultValidationResults();

        Map<String, ValidationResult> allResults = validationResults.getAll();

        ValidationResult errorResult = allResults.get("errorResult");
        assertEquals(Result.ERROR, errorResult.getStatus());
        assertEquals("ERROR", errorResult.getMessage());

        Map<String, ValidationResult> errors = validationResults.getErrors();
        assertEquals(1, errors.size());

        Map<String, ValidationResult> warnings = validationResults.getWarnings();
        assertEquals(0, warnings.size());
    }

    private ValidationResults createDefaultValidationResults() {
        ValidationResults validationResults = new ValidationResults();
        ValidationResult validationResult = new ValidationResult(Result.OK, "OK");
        validationResults.addValidationResult("testResult", validationResult);
        validationResult = new ValidationResult(Result.ERROR, "ERROR");
        validationResults.addValidationResult("errorResult", validationResult);
        return validationResults;
    }

    @Test
    public void testRepeatedValidation() {
        ValidationResults validationResults = new ValidationResults();
        ValidationResult validationResult = new ValidationResult(Result.OK, "OK");
        validationResults.addValidationResult("testResult", validationResult);

        Map<String, ValidationResult> allResults = validationResults.getAll();
        assertEquals(1, allResults.size());
        ValidationResult testResult = allResults.get("testResult");
        assertEquals(Result.OK, testResult.getStatus());
        assertEquals("OK", testResult.getMessage());

        // Test that warning will rewrite ok result
        validationResult = new ValidationResult(Result.WARNING, "WARNING");
        validationResults.addValidationResult("testResult", validationResult);

        allResults = validationResults.getAll();
        assertEquals(1, allResults.size());
        testResult = allResults.get("testResult");
        assertEquals(Result.WARNING, testResult.getStatus());
        assertEquals("WARNING", testResult.getMessage());

        // Test that Error will rewrite warning result
        validationResult = new ValidationResult(Result.ERROR, "ERROR");
        validationResults.addValidationResult("testResult", validationResult);

        allResults = validationResults.getAll();
        assertEquals(1, allResults.size());
        testResult = allResults.get("testResult");
        assertEquals(Result.ERROR, testResult.getStatus());
        assertEquals("ERROR", testResult.getMessage());

        // Test that Warning and ok results won't rewrite error result
        validationResult = new ValidationResult(Result.WARNING, "WARN");
        validationResults.addValidationResult("testResult", validationResult);
        validationResult = new ValidationResult(Result.OK, "OK");
        validationResults.addValidationResult("testResult", validationResult);

        allResults = validationResults.getAll();
        assertEquals(1, allResults.size());
        testResult = allResults.get("testResult");
        assertEquals(Result.ERROR, testResult.getStatus());
        assertEquals("ERROR", testResult.getMessage());
    }

    @Test
    public void testValidateProperties() throws Throwable {
        ValidationProperties props = new ValidationProperties("props");
        props.init();

        ValidationResults results = ValidationHelper.validateProperties(props);
        assertEquals(1, results.getErrors().size());

        props.nestedProperties.propertyToValidate.setValue("abc");
        results = ValidationHelper.validateProperties(props);
        assertEquals(0, results.getErrors().size());
    }

}
