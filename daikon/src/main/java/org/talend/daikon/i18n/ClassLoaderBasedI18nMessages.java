// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class provides a {@link ResourceBundle} string handler with formatted messages using java.text.MessageFormat It will
 * also work with a user defined {@link LocaleProvider} that will be used for each message retrieval. This way web applications
 * may provide their LocalProvider to use dynamic locale according to the current Request for instance.
 */
public class ClassLoaderBasedI18nMessages extends I18nMessages {

    private String baseName;

    private String unknownKeyPrefix;

    transient private ClassLoader classLoader;

    /**
     * return the value associated to the key found in the bundle associated to the localeProvider and the baseName
     *
     * @param localeProvider, if null the java.util.Locale.getDefault() shall be used
     * @param classloader, classloader used to find the resource with the baseName
     * @param baseName, used to create the underlying resource bundle, see {@link ResourceBundle#getBundle(String)}
     * @param unknowKeyPrefix string used to prefix the returned key if the value was not found (if null then an empty
     * String is used)
     */
    public ClassLoaderBasedI18nMessages(LocaleProvider localeProvider, ClassLoader classloader, String baseName,
            String unknowKeyPrefix) {
        super(localeProvider);
        this.classLoader = classloader;
        this.baseName = baseName;
        this.unknownKeyPrefix = unknowKeyPrefix == null ? "" : unknowKeyPrefix; //$NON-NLS-1$
    }

    /**
     * @see #ClassLoaderBasedI18nMessages(LocaleProvider, ClassLoader, String, String)
     */
    public ClassLoaderBasedI18nMessages(LocaleProvider localeProvider, ClassLoader classloader, String baseName) {
        this(localeProvider, classloader, baseName, null);
    }

    /**
     * @see #ClassLoaderBasedI18nMessages(LocaleProvider, ClassLoader, String, String)
     */
    public ClassLoaderBasedI18nMessages(LocaleProvider localeProvider, String baseName, String unknowKeyPrefix) {
        this(localeProvider, I18nMessages.class.getClassLoader(), baseName, unknowKeyPrefix);
    }

    /**
     * @see #ClassLoaderBasedI18nMessages(LocaleProvider, ClassLoader, String, String)
     */
    public ClassLoaderBasedI18nMessages(LocaleProvider localeProvider, String baseName) {
        this(localeProvider, baseName, null);
    }

    /**
     * @see #ClassLoaderBasedI18nMessages(LocaleProvider, ClassLoader, String, String)
     */
    public ClassLoaderBasedI18nMessages(String baseName, String unknowKeyPrefix) {
        this(null, baseName, unknowKeyPrefix);
    }

    /**
     * @see #ClassLoaderBasedI18nMessages(LocaleProvider, ClassLoader, String, String)
     */
    public ClassLoaderBasedI18nMessages(String baseName) {
        this(baseName, null);
    }

    @Override
    public String getMessage(String key, Object... arguments) {
        // get the ResouceBundle Value
        try {
            return getFormattedMessage(key, classLoader, baseName, arguments);

        } catch (MissingResourceException mre) {
            return unknownKeyPrefix + key;
        }
    }

    /**
     * Create an new {@link I18nMessages} based on the current instance localProvider. The same as same as
     * {@link #ClassLoaderBasedI18nMessages(LocaleProvider, String, String)}
     * 
     * @param aClassLoader, classloader used to find the resource with the baseName
     * @param aBaseName, used to create the underlying resource bundle, see {@link ResourceBundle#getBundle(String)}
     * @return a new instance.
     */
    public I18nMessages createNew(ClassLoader aClassLoader, String aBaseName) {
        return new ClassLoaderBasedI18nMessages(this.localeProvider, aClassLoader, aBaseName);
    }

    public String getBaseName() {
        return this.baseName;
    }

}
