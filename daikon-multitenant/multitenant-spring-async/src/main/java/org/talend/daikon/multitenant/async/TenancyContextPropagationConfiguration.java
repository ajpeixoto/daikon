// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.multitenant.async;

import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.talend.daikon.logging.event.field.MdcKeys;
import org.talend.daikon.multitenant.context.TenancyContext;
import org.talend.daikon.multitenant.context.TenancyContextHolder;

@AutoConfiguration
@ConditionalOnClass(TenancyContextHolder.class)
public class TenancyContextPropagationConfiguration {

    @Bean
    public ContextPropagatorFactory tenancyContextPropagatorProvider() {
        return TenancyContextPropagator::new;
    }

    private static class TenancyContextPropagator implements ContextPropagator {

        private TenancyContext tenancyContext;

        @Override
        public void captureContext() {
            tenancyContext = TenancyContextHolder.getContext();
        }

        @Override
        public void setupContext() {
            TenancyContextHolder.setContext(tenancyContext);
            setMdc(tenancyContext);

        }

        @Override
        public void restoreContext() {
            TenancyContextHolder.clearContext();
            removeMdc();
        }

        private static void setMdc(TenancyContext tenancyContext) {
            if (tenancyContext != null && tenancyContext.getOptionalTenant().isPresent()) {
                MDC.put(MdcKeys.ACCOUNT_ID, String.valueOf(tenancyContext.getTenant().getIdentity()));
            }
        }

        private static void removeMdc() {
            MDC.remove(MdcKeys.ACCOUNT_ID);
        }
    }

}
