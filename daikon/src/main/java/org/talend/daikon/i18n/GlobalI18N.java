// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.i18n;

/**
 * Global context that provides I18n service.
 */
public class GlobalI18N {

    protected static I18nMessageProvider i18nMessageProvider;

    /**
     * Get the current MessageProvider. this may be set by the current container or set manually by calling
     * {@link GlobalI18N#createI18nMessageProvider(LocaleProvider)}
     * 
     * @return a message provider, never null (by default it uses the default Locale)
     */
    public static I18nMessageProvider getI18nMessageProvider() {
        // create a default provider if none was created by the container (OSGI or Spring).
        // this should only be the case for non container calls.
        if (i18nMessageProvider == null) {
            // we are using the default Locale here.
            i18nMessageProvider = new I18nMessageProvider() {

                @Override
                protected LocaleProvider getLocaleProvider() {
                    return null;
                }
            };
        }
        return i18nMessageProvider;
    }

    /**
     * Create a static MessageProvider using the given LocalProvider. Should only be used outside a container such as
     * OSGI or Spring
     */
    public static void createI18nMessageProvider(final LocaleProvider localeProvider) {
        i18nMessageProvider = new I18nMessageProvider() {

            @Override
            protected LocaleProvider getLocaleProvider() {
                return localeProvider;
            }
        };

    }

}
