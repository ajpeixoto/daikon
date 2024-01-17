// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.assertj.core.api.BDDSoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.junit.jupiter.api.Test;
import org.talend.daikon.definition.Definition;
import org.talend.daikon.definition.service.DefinitionRegistryService;

public abstract class AbstractPropertiesTest {

    // for benchmarking the apis, one suggestion is to use http://openjdk.java.net/projects/code-tools/jmh/.
    @InjectSoftAssertions
    BDDSoftAssertions errorCollector;

    abstract public DefinitionRegistryService getDefinitionRegistry();

    /**
     * checks that all properties created from all the definition in the registry have a proper i18n displayName and
     * title. As well as checking for each Property and nested Properties.
     */
    @Test
    public void testAlli18n() {
        PropertiesTestUtils.assertAlli18nAreSetup(getDefinitionRegistry(), errorCollector);
    }

    /**
     * checks that all definitions provide an image path to an existing image.
     */
    @Test
    public void testAllImages() {
        PropertiesTestUtils.assertAnIconIsSetup(getDefinitionRegistry(), errorCollector);
    }

    /**
     * @deprecated Use {@link #assertComponentIsRegistered(Class, String, Class)} with the expected class types.
     */
    public void assertComponentIsRegistered(String definitionName) {
        Definition definition = getDefinitionRegistry().getDefinitionsMapByType(Definition.class).get(definitionName);
        assertNotNull(definition,
                "Could not find the definition [" + definitionName + "], please check the registered definitions");
    }

    /**
     * Check that the set of Definitions that correspond to requestClass contain the value with the given definitionName
     * and definitionClass.
     *
     * @param requestClass The set of definitions to request by superclass.
     * @param definitionName The name of the definition to request.
     * @param definitionClass The expected type of class returned.
     */
    public void assertComponentIsRegistered(Class<? extends Definition> requestClass, String definitionName,
            Class<? extends Definition> definitionClass) {
        assertThat("Could not find the definition [" + definitionName + ", " + definitionClass + "]",
                getDefinitionRegistry().getDefinitionsMapByType(requestClass),
                hasEntry(is(definitionName), instanceOf(definitionClass)));
    }

}
