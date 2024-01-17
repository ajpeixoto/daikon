// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties.service;

import org.talend.daikon.properties.Properties;

/**
 * A design-time interface to the repository to allow {@link Properties} to be stored.
 *
 * FIXME - this is probably at the wrong level and will move
 */
public interface Repository<T extends Properties> {

    /**
     * Adds the specified {@link Properties} into the design environment.
     *
     * @param properties the {@code Properties} object to add.
     * @param name the name of the collection of properties
     * @param repositoryLocation the repositoryLocation under which this item should be stored (using the name
     * parameter).
     * @param schemaPropertyName the qualified name of the schema property relative to the properties parameter.
     * @return repositoryLocation, a String containing the location where this object was stored.
     */
    String storeProperties(T properties, String name, String repositoryLocation, String schemaPropertyName);

}
