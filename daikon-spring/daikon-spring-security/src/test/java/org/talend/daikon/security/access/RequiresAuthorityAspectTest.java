// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.security.access;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.talend.daikon.exception.TalendRuntimeException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RequiresAuthorityConfiguration.class)
public class RequiresAuthorityAspectTest {

    private static final AnonymousAuthenticationToken GRANTED = new AnonymousAuthenticationToken("granted", //
            new Object(), //
            singleton(new SimpleGrantedAuthority("TestComponentExec")));

    @Autowired
    RequiresTestComponent component;

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void shouldInvokeSuccessfully() {
        SecurityContextHolder.getContext().setAuthentication(GRANTED);
        assertEquals("secret string", component.execute());
    }

    @Test
    public void shouldInvokeSuccessfullyIfFalsyActiveIf() {
        assertEquals("secret string", component.authorityValuesWithFalsyActiveIf());
    }

    @Test
    public void shouldFailToInvokeIfMissingAuthorityAndTruthyActiveIf() {
        try {
            component.mssingAuthorityWithTruthyActiveIf();
            fail("Expected an error.");
        } catch (TalendRuntimeException e) {
            assertEquals(403, e.getCode().getHttpStatus());
        }
    }

    @Test
    public void shouldInvokeSuccessfullyIfAuthorityAndTruthyActiveIf() {
        SecurityContextHolder.getContext().setAuthentication(GRANTED);
        assertEquals("secret string", component.authorityValuesWithTruthyActiveIf());
    }

    @Test
    public void shouldFailToInvoke() {
        try {
            component.execute();
            fail("Expected an error.");
        } catch (TalendRuntimeException e) {
            assertEquals(403, e.getCode().getHttpStatus());
        }
    }

    @Test
    public void shouldUseAuthorityWhenValuesDefined() {
        try {
            component.authorityValuesPriority();
            fail("Expected an error.");
        } catch (TalendRuntimeException e) {
            assertEquals(403, e.getCode().getHttpStatus());
        }
    }

    @Test
    public void shouldInvokeSuccessfullyWithAuthoritiesAgainstValues() {
        SecurityContextHolder.getContext().setAuthentication(GRANTED);
        assertEquals("secret string", component.authoritiesValuesPriority());
    }

    @Test
    public void shouldInvokeSuccessfullyWithAuthorityAgainstValues() {
        SecurityContextHolder.getContext().setAuthentication(GRANTED);
        assertEquals("secret string", component.authorityValuesPriority());
    }

    @Test
    public void shouldInvokeSuccessfullyWithEmptyAuthorityAgainstValues() {
        SecurityContextHolder.getContext().setAuthentication(GRANTED);
        assertEquals("secret string", component.emptyAuthorityValuesPriority());
    }

    @Test
    public void shouldUseValuesDefined() {
        try {
            component.valuesPriority();
            fail("Expected an error.");
        } catch (TalendRuntimeException e) {
            assertEquals(403, e.getCode().getHttpStatus());
        }
    }

    @Test
    public void shouldInvokeSuccessfullyWithEmptyValues() {
        SecurityContextHolder.getContext().setAuthentication(GRANTED);
        assertEquals("secret string", component.emptyValuesPriority());
    }

    @Test
    public void shouldInvokeSuccessfullyWithValues() {
        SecurityContextHolder.getContext().setAuthentication(GRANTED);
        assertEquals("secret string", component.valuesPriority());
    }

    @Test
    public void shouldUseAuthorityWhenValueDefined() {
        try {
            component.authorityValuePriority();
            fail("Expected an error.");
        } catch (TalendRuntimeException e) {
            assertEquals(403, e.getCode().getHttpStatus());
        }
    }

    @Test
    public void shouldInvokeSuccessfullyWithAuthorityAgainstValue() {
        SecurityContextHolder.getContext().setAuthentication(GRANTED);
        assertEquals("secret string", component.authorityValuePriority());
    }

    @Test
    public void shouldInvokeSuccessfullyWithEmptyAuthorityAgainstValue() {
        SecurityContextHolder.getContext().setAuthentication(GRANTED);
        assertEquals("secret string", component.emptyAuthorityValuePriority());
    }

    @Test
    public void shouldFailToInvokeWithFallback() throws Exception {
        assertEquals("", component.executeWithFallback());
    }

    @Test
    public void shouldSuccessfullyInvokeWithFallback() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(GRANTED);
        assertEquals("secret string", component.executeWithFallback());
    }

    @Test
    public void shouldInvokeUnprotected() throws Exception {
        assertEquals("My details", component.getDetails());
    }
}