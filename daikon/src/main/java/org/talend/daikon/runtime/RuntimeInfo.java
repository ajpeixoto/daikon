// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.runtime;

import java.net.URL;
import java.util.List;

/**
 * Encapsulates an implementation of business logic that can be used at runtime.
 *
 * This is usually created by a {@link org.talend.daikon.definition.Definition} and configured by giving a
 * {@link org.talend.daikon.properties.Properties}. The type of the definition defines the requirements that the
 * business logic is required to implement.
 *
 * In order to cache reusable resources associated with a runtime, the{@link #equals(Object)}, {@link #hashCode()} and
 * {@link #toString()} methods should be implemented so that a RuntimeInfo instance can be used as a key.
 *
 * @see org.talend.daikon.sandbox.SandboxedInstance for a mechanism to automatically create an instance using a
 * dedicated classloader that includes the dependencies.
 */
public interface RuntimeInfo {

    /**
     * list all the dependencies required for this component to be executed at runtime.
     * 
     * @return a set of maven uri following the pax-maven uri scheme @see <a
     * href="https://ops4j1.jira.com/wiki/display/paxurl/Mvn+Protocol">https://ops4j1.jira.com/wiki/display/paxurl/
     * Mvn+Protocol</a>
     */
    List<URL> getMavenUrlDependencies();

    /**
     * @return the name of the class to be instantiated to implement the business logic.
     */
    String getRuntimeClassName();
}
