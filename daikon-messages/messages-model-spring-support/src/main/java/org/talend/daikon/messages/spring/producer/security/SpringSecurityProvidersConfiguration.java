// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.spring.producer.security;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.talend.daikon.messages.header.producer.SecurityTokenProvider;
import org.talend.daikon.messages.header.producer.UserProvider;
import org.talend.daikon.messages.spring.producer.DefaultProducerProvidersConfiguration;

@AutoConfiguration
@ConditionalOnClass({ SecurityContextHolder.class })
@AutoConfigureBefore({ DefaultProducerProvidersConfiguration.class })
public class SpringSecurityProvidersConfiguration {

    @Bean
    public UserProvider springSecurityUserProvider() {
        return new UserProvider() {

            @Override
            public String getUserId() {
                return SecurityContextHolder.getContext().getAuthentication().getName();
            }
        };
    }

    @Bean
    public SecurityTokenProvider springSecurityTokenProvider() {
        return new SecurityTokenProvider() {

            @Override
            public String getSecurityToken() {
                return (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
            }
        };
    }
}
