// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.multitenant.async;

import java.util.Set;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

@AutoConfiguration
@ConditionalOnClass(RequestContextHolder.class)
public class RequestContextPropagationConfiguration {

    @Bean
    public ContextPropagatorFactory requestContextPropagatorFactory() {
        return RequestContextPropagator::new;
    }

    private static class RequestContextPropagator implements ContextPropagator {

        private RequestAttributes requestAttributes;

        @Override
        public void captureContext() {
            RequestAttributes currentAttributes = RequestContextHolder.getRequestAttributes();
            if (currentAttributes != null) {
                this.requestAttributes = new SimpleRequestAttributes(currentAttributes);
            } else {
                this.requestAttributes = null;
            }
        }

        @Override
        public void setupContext() {
            if (requestAttributes != null) {
                RequestContextHolder.setRequestAttributes(requestAttributes);
            }
        }

        @Override
        public void restoreContext() {
            if (requestAttributes != null) {
                RequestContextHolder.resetRequestAttributes();
            }
        }
    }

    private static class SimpleRequestAttributes implements RequestAttributes {

        private Table<Integer, String, Object> requestAttributesMap = HashBasedTable.create();

        public SimpleRequestAttributes(RequestAttributes requestAttributes) {
            for (String key : requestAttributes.getAttributeNames(RequestAttributes.SCOPE_REQUEST)) {
                this.requestAttributesMap.put(SCOPE_REQUEST, key,
                        requestAttributes.getAttribute(key, RequestAttributes.SCOPE_REQUEST));
            }
            for (String key : requestAttributes.getAttributeNames(RequestAttributes.SCOPE_SESSION)) {
                this.requestAttributesMap.put(SCOPE_SESSION, key,
                        requestAttributes.getAttribute(key, RequestAttributes.SCOPE_SESSION));
            }
        }

        @Override
        public Object getAttribute(String name, int scope) {
            return requestAttributesMap.get(scope, name);
        }

        @Override
        public void setAttribute(String name, Object value, int scope) {
            this.requestAttributesMap.put(scope, name, value);
        }

        @Override
        public void removeAttribute(String name, int scope) {
            this.requestAttributesMap.remove(scope, name);
        }

        @Override
        public String[] getAttributeNames(int scope) {
            Set<String> names = requestAttributesMap.columnKeySet();
            return names.toArray(new String[names.size()]);
        }

        @Override
        public void registerDestructionCallback(String name, Runnable callback, int scope) {

        }

        @Override
        public Object resolveReference(String key) {
            return null;
        }

        @Override
        public String getSessionId() {
            return null;
        }

        @Override
        public Object getSessionMutex() {
            return null;
        }
    }

}
