// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.serialize.jsonio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.talend.daikon.properties.property.PropertyFactory.newProperty;

import java.util.EnumSet;

import org.junit.jupiter.api.Test;
import org.talend.daikon.properties.PropertiesImpl;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.security.CryptoHelper;
import org.talend.daikon.serialize.SerializerDeserializer;

public class PasswordSerializeTest {

    @Test
    public void testPasswordSerialized() {
        BasicProperties basicProp = new BasicProperties("test");
        basicProp.init();
        basicProp.userPassword.userId.setValue("myUser");
        basicProp.userPassword.password.setValue("myPassword");

        String serialized = basicProp.toSerialized();
        BasicProperties deserializedProp = SerializerDeserializer.fromSerializedPersistent(serialized,
                BasicProperties.class).object;
        assertEquals("myUser", deserializedProp.userPassword.userId.getValue());
        assertEquals("myPassword", deserializedProp.userPassword.password.getValue());
    }

    public class BasicProperties extends PropertiesImpl {

        public UserPasswordProperties userPassword = new UserPasswordProperties("userPassword");

        public NestedProperties nested;

        public BasicProperties(String name) {
            super(name);
        }

        @Override
        public void setupProperties() {
            super.setupProperties();
            nested = new NestedProperties("nested");
            nested.userPassword = userPassword;
        }

        protected void encryptData(Property property, boolean encrypt) {

            if (property.getStoredValue() == null) {
                return;
            }

            if (property.isFlag(Property.Flags.ENCRYPT)) {
                String value = (String) property.getStoredValue();
                CryptoHelper ch = new CryptoHelper(CryptoHelper.PASSPHRASE);
                if (encrypt) {
                    property.setStoredValue(ch.encrypt(value));
                } else {
                    super.encryptData(property, encrypt);
                }
            }
        }
    }

    public class NestedProperties extends PropertiesImpl {

        public UserPasswordProperties userPassword = new UserPasswordProperties("userPassword");

        public NestedProperties(String name) {
            super(name);
        }

    }

    public class UserPasswordProperties extends PropertiesImpl {

        public Property<String> userId = newProperty("userId").setRequired(); //$NON-NLS-1$

        public Property<String> password = newProperty("password").setRequired()
                .setFlags(EnumSet.of(Property.Flags.ENCRYPT, Property.Flags.SUPPRESS_LOGGING));

        public UserPasswordProperties(String name) {
            super(name);
        }

    }

}
