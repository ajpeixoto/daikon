// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.multitenant.web;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.WebAsyncManager;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.talend.daikon.logging.event.field.MdcKeys;
import org.talend.daikon.multitenant.context.TenancyContextHolder;
import org.talend.daikon.multitenant.provider.DefaultTenantProvider;
import org.talend.daikon.multitenant.provider.TenantProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SpringBootApplication
@Import({ MultiTenantApplication.TenancyConfiguration.class,
        MultiTenantApplication.CleanContextCheckerFilterConfiguration.class })
public class MultiTenantApplication {

    public static final String TENANT_HTTP_HEADER = "X-Test-TenantId";

    public static final String MESSAGE = "Hello, World!";

    public static void main(String[] args) { // NOSONAR
        SpringApplication.run(MultiTenantApplication.class, args); // NOSONAR
    }

    @AutoConfiguration
    public static class TenancyConfiguration {

        @Bean
        public TenantProvider tenantProvider() {
            return new DefaultTenantProvider();
        }

        @Bean
        public TenantIdentificationStrategy tenantIdentificationStrategy() {
            HeaderTenantIdentificationStrategy strategy = new HeaderTenantIdentificationStrategy();
            strategy.setHeaderName(TENANT_HTTP_HEADER);
            return strategy;
        }
    }

    @AutoConfiguration
    public static class CleanContextCheckerFilterConfiguration {

        @Bean
        public FilterRegistrationBean cleanContextChecker() {
            FilterRegistrationBean registration = new FilterRegistrationBean();
            CleanContextCheckerFilter filter = new CleanContextCheckerFilter();
            registration.setFilter(filter);
            registration.setOrder(Integer.MIN_VALUE);
            return registration;
        }

    }

    @RestController
    public static class TestRestController {

        @Autowired
        private final SampleRequestHandler sampleRequestHandler;

        public TestRestController(SampleRequestHandler handler) {
            this.sampleRequestHandler = handler;
        }

        @RequestMapping(path = "/sync", method = RequestMethod.GET)
        public String sayHelloSync() {
            return sayHello();
        }

        @RequestMapping(path = "/async", method = RequestMethod.GET)
        public Callable<String> sayHelloAsync() {
            return this::sayHello;
        }

        private String sayHello() {
            this.sampleRequestHandler.onSampleRequestCalled();
            return MESSAGE;
        }
    }

    public static class CleanContextCheckerFilter extends OncePerRequestFilter implements CallableProcessingInterceptor {

        private static final Object KEY = new Object();

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
            asyncManager.registerCallableInterceptor(KEY, this);
            filterChain.doFilter(request, response);
            this.checkContextIsClean();
        }

        @Override
        public <T> void beforeConcurrentHandling(NativeWebRequest request, Callable<T> task) throws Exception {
            // do not verify here, since we are in the HTTP thread and it is already affected by the filter
        }

        @Override
        public <T> void preProcess(NativeWebRequest request, Callable<T> task) throws Exception {
            this.checkContextIsClean();
        }

        @Override
        public <T> void postProcess(NativeWebRequest request, Callable<T> task, Object concurrentResult) throws Exception {
            this.checkContextIsClean();
        }

        @Override
        public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
            return null;
        }

        @Override
        public <T> void afterCompletion(NativeWebRequest request, Callable<T> task) throws Exception {
            this.checkContextIsClean();
        }

        private void checkContextIsClean() {
            assertNull(MDC.get(MdcKeys.ACCOUNT_ID));
            assertFalse(TenancyContextHolder.getContext().getOptionalTenant().isPresent());
        }
    }

    public interface SampleRequestHandler {

        void onSampleRequestCalled();

    }

}
