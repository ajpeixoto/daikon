// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.sandbox.properties;

import java.util.Properties;

public interface StandardPropertiesStrategy {

    /**
     * @return Returns the {@link System#getProperties()} properties used by default in the current running JVM. This method must
     * returns valid properties that can allow a simple Java's main method to be executed without any issue.
     */
    Properties getStandardProperties();

}
