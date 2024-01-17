// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.talend.daikon.exception.TalendRuntimeException;
import org.talend.daikon.exception.error.CommonErrorCodes;
import org.talend.daikon.properties.property.EnumProperty;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;
import org.talend.daikon.properties.property.PropertyValueEvaluator;

public class EnumPropertyTest {

    private final class EnumPropForI18NTest extends EnumProperty {

        private EnumPropForI18NTest(Class zeEnumType, String name) {
            super(zeEnumType, name);
        }
    }

    public enum TestEnum {
        FOO,
        BAR,
        FOOBAR
    }

    @Test
    public void testGetValue() {
        EnumProperty<TestEnum> enumProperty = PropertyFactory.newEnum("prop", TestEnum.class);
        assertThat((List<TestEnum>) enumProperty.getPossibleValues(), contains(TestEnum.FOO, TestEnum.BAR, TestEnum.FOOBAR));
        // test the Stored value as a String
        enumProperty.setStoredValue("FOO");
        assertEquals(TestEnum.FOO, enumProperty.getValue());
    }

    @Test
    public void testGetValueWithEvaluator() {
        EnumProperty<TestEnum> enumProperty = PropertyFactory.newEnum("prop", TestEnum.class);
        enumProperty.setValueEvaluator(new PropertyValueEvaluator() {

            @Override
            public <T> T evaluate(Property<T> property, Object storedValue) {
                return (T) TestEnum.valueOf((String) storedValue);
            }
        });
        enumProperty.setStoredValue("FOO");
        assertEquals(TestEnum.FOO, enumProperty.getValue());
    }

    @Test
    public void testI18nForEnums() {
        EnumProperty<TestEnum> enumProperty = new EnumPropForI18NTest(TestEnum.class, "enumProperty");
        assertEquals("fo o", enumProperty.getPossibleValuesDisplayName(TestEnum.FOO));
        assertEquals("ba r", enumProperty.getPossibleValuesDisplayName(TestEnum.BAR));
        assertEquals("fooba r", enumProperty.getPossibleValuesDisplayName(TestEnum.FOOBAR));
        try {
            enumProperty.getPossibleValuesDisplayName("I am your father");
            fail("exception should have been thrown.");
        } catch (TalendRuntimeException e) {
            assertEquals(CommonErrorCodes.UNEXPECTED_ARGUMENT, e.getCode());
        }
    }

}
