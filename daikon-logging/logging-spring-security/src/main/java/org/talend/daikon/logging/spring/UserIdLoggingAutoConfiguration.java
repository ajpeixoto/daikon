// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.logging.spring;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Spring boot auto-configuration class to automatically set the appropriate {@link org.apache.log4j.MDC}
 * for current user id.
 *
 * It works with both synchronous and asynchronous endpoints.
 */
@AutoConfiguration
@ConditionalOnClass(SecurityContextHolder.class)
public class UserIdLoggingAutoConfiguration {

    public int USER_ID_LOGS_FILTER_ORDER = SecurityProperties.DEFAULT_FILTER_ORDER + 1;

    @Bean
    @ConditionalOnMissingBean(UserIdExtractor.class)
    public UserIdExtractor userIdExtractor() {
        return new UserIdExtractorImpl();
    }

    @Bean
    public FilterRegistrationBean userIdLoggingFilter(UserIdExtractor userIdExtractor) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        UserIdLoggingFilter filter = new UserIdLoggingFilter(userIdExtractor);
        registration.setFilter(filter);
        registration.setOrder(USER_ID_LOGS_FILTER_ORDER);
        return registration;
    }
}