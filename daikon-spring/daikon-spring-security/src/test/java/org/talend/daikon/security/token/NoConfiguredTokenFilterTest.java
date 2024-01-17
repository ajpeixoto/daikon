// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.security.token;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NoConfiguredTokenFilterTest {

    private final NoConfiguredTokenFilter filter = new NoConfiguredTokenFilter(new AntPathRequestMatcher("/actuator/**"));

    private final HttpServletRequest request;

    private final FilterChain filterChain;

    private final HttpServletResponse response;

    public NoConfiguredTokenFilterTest() {
        filterChain = mock(FilterChain.class);
        response = mock(HttpServletResponse.class);
        request = mock(HttpServletRequest.class);
    }

    @BeforeEach
    public void setUp() {
        reset(response, request, filterChain);
    }

    @Test
    public void shouldSet503() throws IOException, ServletException {
        // given
        when(request.getServletPath()).thenReturn("/actuator/prometheus");

        // when
        filter.doFilter(request, response, filterChain);

        // then
        verify(response, times(1)).setStatus(eq(503));
    }

    @Test
    public void shouldNotSet503() throws IOException, ServletException {
        // given
        when(request.getServletPath()).thenReturn("/service");

        // when
        filter.doFilter(request, response, filterChain);

        // then
        verify(response, never()).setStatus(eq(503));
    }
}