// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.i18n;

import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class CustomExtension implements AfterEachCallback, BeforeEachCallback {

    private Runnable callback;

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        callback = ProviderLocator.instance().register(Thread.currentThread().getContextClassLoader(), new BaseProvider() {

            @Override
            protected ResourceBundle createBundle(final String baseName, final Locale locale) {
                return new BaseBundle() {

                    private int incr = 1;

                    @Override
                    protected Set<String> doGetKeys() {
                        return Collections.singleton("thekey");
                    }

                    @Override
                    protected Object handleGetObject(final String key) {
                        return "thekey".equals(key) ? "thevalue" + incr++ : null;
                    }
                };
            }

            @Override
            protected boolean supports(final String baseName) {
                return "test".equals(baseName);
            }
        });
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        callback.run();
    }
}
