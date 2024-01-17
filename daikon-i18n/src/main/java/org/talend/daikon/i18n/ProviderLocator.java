// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.i18n;

import static java.lang.ClassLoader.getSystemClassLoader;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.spi.ResourceBundleControlProvider;

public class ProviderLocator {

    private static final ProviderLocator LOCATOR = new ProviderLocator();

    private final Map<ClassLoader, ResourceBundleControlProvider> providers = new ConcurrentHashMap<>();

    public static ProviderLocator instance() {
        return LOCATOR;
    }

    public Provider current() {
        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        if (tccl == null) {
            tccl = getSystemClassLoader();
        }
        while (tccl != null) {
            final ResourceBundleControlProvider impl = providers.get(tccl);
            if (impl != null) {
                return new Provider(impl, tccl);
            }
            if (tccl == tccl.getParent()) {
                return null;
            }
            tccl = tccl.getParent();
        }
        return null;
    }

    /**
     * @param loader
     * the loader identifying the current context.
     * @param customProvider
     * the bundle control provider to associate with this loader.
     * @return a callback to call when you don't need the custom provider anymore.
     */
    public Runnable register(final ClassLoader loader, final ResourceBundleControlProvider customProvider) {
        if (providers.putIfAbsent(loader, customProvider) != null) {
            throw new IllegalArgumentException("A custom ResourceBundleControlProvider is already registered for " + loader);
        }
        return () -> providers.remove(loader, customProvider);
    }

    public Runnable register(final ClassLoader loader, final Predicate<String> baseNameFilter,
            final BiFunction<String, Locale, ResourceBundle> factory) {
        return register(loader, new BaseProvider() {

            @Override
            protected ResourceBundle createBundle(final String baseName, final Locale locale) {
                return factory.apply(baseName, locale);
            }

            @Override
            protected boolean supports(final String baseName) {
                return baseNameFilter.test(baseName);
            }
        });
    }

    static class Provider {

        private final ResourceBundleControlProvider provider;

        private final ClassLoader loader;

        Provider(final ResourceBundleControlProvider provider, final ClassLoader loader) {
            this.provider = provider;
            this.loader = loader;
        }

        public ResourceBundleControlProvider getProvider() {
            return provider;
        }
    }
}
