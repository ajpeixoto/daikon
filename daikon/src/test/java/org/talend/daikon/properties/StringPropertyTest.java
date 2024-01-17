// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.talend.daikon.NamedThing;
import org.talend.daikon.SimpleNamedThing;
import org.talend.daikon.exception.TalendRuntimeException;
import org.talend.daikon.exception.error.CommonErrorCodes;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.Property.Flags;
import org.talend.daikon.properties.property.StringProperty;
import org.talend.daikon.security.CryptoHelper;

public class StringPropertyTest {

    @Test
    public void testSetPossibleValuesWithNamedThing() {
        StringProperty stringProperty = new StringProperty("foo");
        ArrayList<NamedThing> namedThings = new ArrayList<>();
        namedThings.add(new SimpleNamedThing("foo", "fo o"));
        namedThings.add(new SimpleNamedThing("bar", "ba r"));
        namedThings.add(new SimpleNamedThing("a null", null));
        stringProperty.setPossibleNamedThingValues(namedThings);
        assertThat((List<String>) stringProperty.getPossibleValues(), contains("foo", "bar", "a null"));
        assertEquals("fo o", stringProperty.getPossibleValuesDisplayName("foo"));
        assertEquals("ba r", stringProperty.getPossibleValuesDisplayName("bar"));
        assertEquals("a null", stringProperty.getPossibleValuesDisplayName("a null"));
        // test that with unknown value, an execption is thrown
        try {
            stringProperty.getPossibleValuesDisplayName("not existing value");
            fail("exception should have been thrown.");
        } catch (TalendRuntimeException e) {
            assertEquals(CommonErrorCodes.UNEXPECTED_ARGUMENT, e.getCode());
        }
    }

    @Test
    public void testSetPossibleValuesWithNamedThingDefaultMethod() {
        StringProperty stringProperty = new StringProperty("foo");
        ArrayList<NamedThing> namedThings = new ArrayList<>();
        namedThings.add(new SimpleNamedThing("foo", "fo o"));
        namedThings.add(new SimpleNamedThing("bar", "ba r"));
        namedThings.add(new SimpleNamedThing("a null", null));
        stringProperty.setPossibleValues(namedThings);
        assertThat((List<String>) stringProperty.getPossibleValues(), contains("foo", "bar", "a null"));
        assertEquals("fo o", stringProperty.getPossibleValuesDisplayName("foo"));
        assertEquals("ba r", stringProperty.getPossibleValuesDisplayName("bar"));
        assertEquals("a null", stringProperty.getPossibleValuesDisplayName("a null"));
        // test that with unknown value, an execption is thrown
        try {
            stringProperty.getPossibleValuesDisplayName("not existing value");
            fail("exception should have been thrown.");
        } catch (TalendRuntimeException e) {
            assertEquals(CommonErrorCodes.UNEXPECTED_ARGUMENT, e.getCode());
        }
    }

    @Test
    public void testSetPossibleValuesNotNamedNamedThing() {
        StringProperty stringProperty = new StringProperty("foo") {// in order to have i18n related to this class
        };
        stringProperty.setPossibleValues("possible.value");
        assertEquals("possible.value", stringProperty.getPossibleValuesDisplayName("possible.value"));
        stringProperty.setPossibleValues("possible.value.2");
        assertEquals("possible value 2 i18n", stringProperty.getPossibleValuesDisplayName("possible.value.2"));

    }

    @Test
    public void testEncryptStringProp() {
        StringProperty stringProperty = new StringProperty("foo") {// in order to have i18n related to this class

            @Override
            public void encryptStoredValue(boolean encrypt) {

                if (isFlag(Property.Flags.ENCRYPT)) {
                    String value = (String) getStoredValue();
                    CryptoHelper ch = new CryptoHelper(CryptoHelper.PASSPHRASE);
                    if (encrypt) {
                        setStoredValue(ch.encrypt(value));
                    } else {
                        super.encryptStoredValue(encrypt);
                    }
                }
            }
        };
        stringProperty.setValue("foo");
        assertEquals("foo", stringProperty.getValue());
        stringProperty.encryptStoredValue(true);
        // value should be the same cause we did not set the Encrypt flag
        assertEquals("foo", stringProperty.getValue());
        stringProperty.encryptStoredValue(false);
        // value should be the same cause we did not set the Encrypt flag
        assertEquals("foo", stringProperty.getValue());

        // make the property to be encrypted
        stringProperty.setFlags(EnumSet.of(Flags.ENCRYPT));
        assertEquals("foo", stringProperty.getValue());
        stringProperty.encryptStoredValue(true);
        assertNotEquals("foo", stringProperty.getValue());
        stringProperty.encryptStoredValue(false);
        assertEquals("foo", stringProperty.getValue());
    }

    @Test
    public void testEncryptionFailure() {
        assertThrows(TalendRuntimeException.class, () -> {
            StringProperty stringProperty = new StringProperty("foo") {// in order to have i18n related to this class
            };
            stringProperty.setValue("foo");
            assertEquals("foo", stringProperty.getValue());
            stringProperty.encryptStoredValue(true);
            // value should be the same cause we did not set the Encrypt flag
            assertEquals("foo", stringProperty.getValue());
            stringProperty.encryptStoredValue(false);
            // value should be the same cause we did not set the Encrypt flag
            assertEquals("foo", stringProperty.getValue());

            // make the property to be encrypted
            stringProperty.setFlags(EnumSet.of(Flags.ENCRYPT));
            assertEquals("foo", stringProperty.getValue());
            stringProperty.encryptStoredValue(true);
        });
    }
}
