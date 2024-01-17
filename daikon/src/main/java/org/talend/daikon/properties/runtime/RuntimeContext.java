// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties.runtime;

import org.talend.daikon.properties.Properties;

/**
 * Represents Runtime context, in which {@link Properties} are used.
 * This interface can be used to pass some information from Runtime to {@link Properties}'s trigger methods:
 * {@code before<PropertyName>}, {@code after<PropertyName>} etc
 */
public interface RuntimeContext {

    /**
     * Return a data value for given key or null if there is no such data
     * 
     * @param key data value key
     * @return data value for given key or null
     */
    Object getData(String key);

}
