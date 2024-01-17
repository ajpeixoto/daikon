// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.spring.consumer.security;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.talend.daikon.messages.header.consumer.UserIdSetter;
import org.talend.daikon.messages.spring.consumer.DefaultConsumerSettersConfiguration;

@AutoConfiguration
@ConditionalOnClass(SecurityContextHolder.class)
@AutoConfigureBefore({ DefaultConsumerSettersConfiguration.class })
public class SpringSecuritySettersConfiguration {

    @Bean
    public UserIdSetter userIdSetter() {
        return userId -> {
            Authentication authentication = null;
            SecurityContextHolder.getContext().setAuthentication(authentication);
        };
    }
}
