// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.sandbox.properties;
// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.talend.daikon.runtime.RuntimeInfo;

public class ClassLoaderIsolatedSystemPropertiesStaticInitTest {

    public static final int TEST_TIMES = 5;

    private Properties previous;

    private class TestRuntime implements RuntimeInfo {

        private String name;

        public TestRuntime(String name) {
            this.name = name;
        }

        @Override
        public String getRuntimeClassName() {
            return name;
        }

        @Override
        public List<URL> getMavenUrlDependencies() {
            return Collections.EMPTY_LIST;
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
        previous = System.getProperties();
        assertFalse(previous instanceof ClassLoaderIsolatedSystemProperties);
    }

    @AfterEach
    public void tearDown() throws Exception {
        System.setProperties(previous);
    }

}
