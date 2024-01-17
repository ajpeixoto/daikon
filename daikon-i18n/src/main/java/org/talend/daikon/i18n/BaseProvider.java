// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.i18n;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.spi.ResourceBundleControlProvider;

public abstract class BaseProvider implements ResourceBundleControlProvider {

    @Override
    public ResourceBundle.Control getControl(final String baseName) {
        return supports(baseName) ? new BaseControl() {

            @Override
            public ResourceBundle newBundle(final String baseName, final Locale locale, final String format,
                    final ClassLoader loader, final boolean reload)
                    throws IllegalAccessException, InstantiationException, IOException {
                return createBundle(baseName, locale);
            }
        } : null;
    }

    protected abstract ResourceBundle createBundle(String baseName, Locale locale);

    protected abstract boolean supports(String baseName);
}
