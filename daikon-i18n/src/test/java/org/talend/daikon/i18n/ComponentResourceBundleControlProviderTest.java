// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.i18n;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(CustomExtension.class)
public class ComponentResourceBundleControlProviderTest {

    @Test
    public void useCustomProvider() {
        for (int i = 0; i < 10; i++) {
            final ResourceBundle bundle = ResourceBundle.getBundle("test", Locale.ENGLISH);
            assertNotNull(bundle);
            assertEquals("thevalue" + (i + 1), bundle.getString("thekey"));
        }
    }
}
