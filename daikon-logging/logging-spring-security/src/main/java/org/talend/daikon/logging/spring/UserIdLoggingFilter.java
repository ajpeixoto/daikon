// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.logging.spring;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.WebAsyncManager;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.talend.daikon.logging.event.field.MdcKeys;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter implementation.
 *
 * Uses {@link SecurityContextHolder} to access the current spring security context.
 */
class UserIdLoggingFilter extends OncePerRequestFilter {

    private static final Object CALLABLE_INTERCEPTOR_KEY = new Object();

    private UserIdExtractor userIdExtractor = new UserIdExtractorImpl();

    public UserIdLoggingFilter() {
    }

    public UserIdLoggingFilter(UserIdExtractor userIdExtractor) {
        this.userIdExtractor = userIdExtractor;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            FilterChain filterChain) throws ServletException, IOException {
        setMdc(userIdExtractor);

        WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(httpServletRequest);

        UserIdLoggingFilter tenancyProcessingInterceptor = (UserIdLoggingFilter) asyncManager
                .getCallableInterceptor(CALLABLE_INTERCEPTOR_KEY);
        if (tenancyProcessingInterceptor == null) {
            asyncManager.registerCallableInterceptor(CALLABLE_INTERCEPTOR_KEY,
                    new UserIdCallableProcessingInterceptorAdapter(userIdExtractor));
        }
        try {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } finally {
            removeMdc();
        }

    }

    private static class UserIdCallableProcessingInterceptorAdapter implements CallableProcessingInterceptor {

        private UserIdExtractor userIdExtractor;

        public UserIdCallableProcessingInterceptorAdapter(UserIdExtractor userIdExtractor) {
            this.userIdExtractor = userIdExtractor;
        }

        @Override
        public <T> void preProcess(NativeWebRequest request, Callable<T> task) throws Exception {
            setMdc(userIdExtractor);
        }

        @Override
        public <T> void postProcess(NativeWebRequest request, Callable<T> task, Object concurrentResult) throws Exception {
            removeMdc();
        }
    }

    private static void setMdc(UserIdExtractor userIdExtractor) {
        userIdExtractor.extract().ifPresent(userId -> MDC.put(MdcKeys.USER_ID, userId));
    }

    private static void removeMdc() {
        MDC.remove(MdcKeys.USER_ID);
    }
}
