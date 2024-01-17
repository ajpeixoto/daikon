// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.serialize.javadefault;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;

import org.junit.jupiter.api.Test;
import org.talend.daikon.serialize.FullExampleProperties;
import org.talend.daikon.serialize.FullExampleTestUtil;

public class SerializeDeserializeTest {

    @Test
    public void test() throws IOException, ClassNotFoundException, ParseException {
        FullExampleProperties properties = FullExampleTestUtil.createASetupFullExampleProperties();

        ByteArrayOutputStream outStore = new ByteArrayOutputStream();
        ObjectOutputStream ser = new ObjectOutputStream(outStore);
        ser.writeObject(properties);
        ser.close();
        ByteArrayInputStream inStore = new ByteArrayInputStream(outStore.toByteArray());
        outStore.close();

        ObjectInputStream des = new ObjectInputStream(inStore);
        FullExampleProperties copiedProperties = (FullExampleProperties) des.readObject();
        des.close();
        inStore.close();

        FullExampleTestUtil.assertPropertiesValueAreEquals(properties, copiedProperties);
    }

}
