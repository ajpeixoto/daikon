// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.multitenant.async;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class ContextAwareCallable<T> implements Callable<T> {

    private final Callable<T> callable;

    private final List<ContextPropagator> contextPropagators;

    ContextAwareCallable(Callable<T> callable, List<ContextPropagatorFactory> contextPropagatorProvider) {
        this.callable = callable;
        this.contextPropagators = contextPropagatorProvider.stream().map(Supplier::get).collect(Collectors.toList());
        this.contextPropagators.forEach(ContextPropagator::captureContext);
    }

    @Override
    public T call() throws Exception {
        this.contextPropagators.forEach(ContextPropagator::setupContext);
        try {
            return this.callable.call();
        } finally {
            this.contextPropagators.forEach(ContextPropagator::restoreContext);
        }
    }
}
