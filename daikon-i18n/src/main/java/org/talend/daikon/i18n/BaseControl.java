// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.i18n;

import static java.util.Collections.singletonList;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class BaseControl extends ResourceBundle.Control {

    private static final List<String> FORMAT = singletonList("talend");

    @Override
    public List<String> getFormats(final String baseName) {
        return FORMAT;
    }

    @Override
    public Locale getFallbackLocale(final String baseName, final Locale locale) {
        return Locale.ENGLISH;
    }

    @Override
    public abstract ResourceBundle newBundle(final String baseName, final Locale locale, final String format,
            final ClassLoader loader, final boolean reload) throws IllegalAccessException, InstantiationException, IOException;

    @Override // can be overriden to limit the locales (only language handling for instance)
    public String toBundleName(final String baseName, final Locale locale) {
        return super.toBundleName(baseName, locale);
    }

    @Override // don't expire, ensure the impl handles the caching
    public long getTimeToLive(final String baseName, final Locale locale) {
        return TTL_NO_EXPIRATION_CONTROL;
    }

    @Override
    public boolean needsReload(final String baseName, final Locale locale, final String format, final ClassLoader loader,
            final ResourceBundle bundle, final long loadTime) {
        return false;
    }
}
