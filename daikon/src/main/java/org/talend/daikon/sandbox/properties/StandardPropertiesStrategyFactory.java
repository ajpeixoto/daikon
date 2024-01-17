// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.sandbox.properties;

/**
 * Chooses and creates instances of {@link StandardPropertiesStrategy} depending on JVM.
 */
public class StandardPropertiesStrategyFactory {

    private StandardPropertiesStrategyFactory() {
    }

    /**
     * @return Returns a {@link StandardPropertiesStrategy} valid for current running JVM.
     */
    public static StandardPropertiesStrategy create() {
        // Only support Sun/Oracle (for the time being).
        return new SunOracleStandardPropertiesStrategy();
    }
}
