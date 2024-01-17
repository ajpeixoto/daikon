// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.ccf.context.configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.talend.daikon.spring.ccf.context.M2MContextManager;
import org.talend.daikon.spring.ccf.context.M2MFunctionalContextAspect;
import org.talend.daikon.spring.ccf.context.TenantParameterExtractor;
import org.talend.daikon.spring.ccf.context.TenantParameterExtractorImpl;
import org.talend.daikon.spring.ccf.context.utils.ScimRequestUtilities;
import org.talend.iam.im.scim.client.UserClient;

import lombok.extern.slf4j.Slf4j;

@AutoConfiguration
@EnableCaching
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Slf4j
public class M2MFunctionalContextAutoConfiguration {

    public static final String CACHE_NAME = "ccfScimCache";

    @Bean
    @ConditionalOnMissingBean(TenantParameterExtractor.class)
    public TenantParameterExtractor defaultRequestParamExtractor() {
        return new TenantParameterExtractorImpl();
    }

    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager defaultCacheManager(@Value("${spring.ccf.context.cache.name:ccfScimCache}") String cacheName) {
        log.info("No CacheManager found, using the default ConcurrentMapCacheManager.");
        return new ConcurrentMapCacheManager(cacheName);
    }

    @Bean
    public M2MFunctionalContextAspect m2MFunctionalContextAspect(M2MContextManager m2MContextManager,
            TenantParameterExtractor tenantParameterExtractor, ScimRequestUtilities scimRequestUtilities) {
        return new M2MFunctionalContextAspect(m2MContextManager, tenantParameterExtractor, scimRequestUtilities);
    }

    @CacheEvict(allEntries = true, cacheNames = { CACHE_NAME })
    @Scheduled(fixedDelayString = "${spring.ccf.context.cache.ttl:60}", timeUnit = TimeUnit.MINUTES, initialDelayString = "${spring.ccf.context.cache.ttl:60}")
    public void reportCacheEvict() {
        log.info("Flushing CCF Scim Cache");
    }

    @Bean
    public ScimRequestUtilities scimRequestUtilities(UserClient userClient) {
        return new ScimRequestUtilities(userClient);
    }
}
