// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.multitenant.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.junit.jupiter.api.Test;
import org.talend.daikon.multitenant.context.DefaultTenancyContext;
import org.talend.daikon.multitenant.provider.DefaultTenant;

/**
 * Test serialization of tenancy components
 *
 * @author David Green (Tasktop Technologies Inc.)
 */
public class TenancySerializationTest {

    @Test
    public void testDefaultTenantSerializable() {
        DefaultTenant defaultTenant = new DefaultTenant("id", "data");
        assertSerializable(defaultTenant);
    }

    @Test
    public void testDefaultTenancyContextSerializable() {
        DefaultTenancyContext context = new DefaultTenancyContext();
        assertSerializable(context);
    }

    private void assertSerializable(Object o) {
        assertTrue(o instanceof Serializable);
        Object copy;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(o);
            objectOutputStream.close();

            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
            copy = objectInputStream.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            fail("Cannot serialize object " + o);
            throw new IllegalStateException(e);
        }
        assertNotNull(copy);
        assertEquals(o, copy);
    }
}
