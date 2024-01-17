// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.metrics.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import java.io.IOException;
import java.io.InputStream;

import jakarta.servlet.http.Part;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PartWrapperTest {

    private PartWrapper partWrapper;

    private Part delegate;

    @BeforeEach
    public void setUp() {
        delegate = mock(Part.class);
        partWrapper = new PartWrapper(delegate);
    }

    @Test
    public void shouldWrapInputStream() throws IOException {
        // given
        when(delegate.getInputStream()).thenReturn(mock(InputStream.class));

        // when
        final InputStream inputStream = partWrapper.getInputStream();

        // then
        assertEquals(inputStream.getClass(), MeteredInputStream.class);
    }

    @Test
    public void shouldCallDelegateContentType() {
        // when
        partWrapper.getContentType();

        // then
        verify(delegate, times(1)).getContentType();
    }

    @Test
    public void shouldCallDelegateName() {
        // when
        partWrapper.getName();

        // then
        verify(delegate, times(1)).getName();
    }

    @Test
    public void shouldCallDelegateSize() {
        // when
        partWrapper.getSize();

        // then
        verify(delegate, times(1)).getSize();
    }

    @Test
    public void shouldCallDelegateWrite() throws IOException {
        // when
        partWrapper.write("");

        // then
        verify(delegate, times(1)).write(any());
    }

    @Test
    public void shouldCallDelegateDelete() throws IOException {
        // when
        partWrapper.delete();

        // then
        verify(delegate, times(1)).delete();
    }

    @Test
    public void shouldCallDelegateHeader() {
        // when
        partWrapper.getHeader("");

        // then
        verify(delegate, times(1)).getHeader(any());
    }

    @Test
    public void shouldCallDelegateHeaders() {
        // when
        partWrapper.getHeaderNames();

        // then
        verify(delegate, times(1)).getHeaderNames();
    }

}