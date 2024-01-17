// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.i18n;

import java.util.Locale;

/**
 * Provide a specific Locale for {@link I18nMessages}.
 */
public interface LocaleProvider {

    /**
     * @return the Locale to be used by {@link I18nMessages}.
     */
    Locale getLocale();
}
