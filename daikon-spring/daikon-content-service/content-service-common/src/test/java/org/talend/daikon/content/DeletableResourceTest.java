// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.content;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class DeletableResourceTest extends DeletableLoaderResourceTests {

    protected static final String LOCATION = "file.txt";

    protected DeletableResource resource;

    @BeforeEach
    public void setUp() throws Exception {
        resource = resolver.getResource(LOCATION);
        try (OutputStream outputStream = resource.getOutputStream()) {
            outputStream.write("test".getBytes());
        }

    }

    @AfterEach
    public void tearDown() {
        try {
            resource.delete();
        } catch (IOException e) {
            // Ignored
        }
    }

    @Test
    public void shouldDelete() throws Exception {
        // given
        assertTrue(resource.exists());
        // when
        resource.delete();
        // then
        assertFalse(resolver.getResource(LOCATION).exists());
        assertFalse(resource.exists());
    }

    @Test
    public void shouldMoveResource() throws Exception {
        // when
        resource.move("newLocation.txt");

        // then
        assertFalse(resolver.getResource(LOCATION).exists());
        assertTrue(resolver.getResource("newLocation.txt").exists());
        assertEquals("test", IOUtils.toString(resolver.getResource("newLocation.txt").getInputStream()));
    }

    @Test
    public void shouldExist() {
        assertTrue(resource.exists());
    }

    @Test
    public void shouldBeReadable() {
        assertTrue(resource.isReadable());
    }

    @Test
    public void shouldNotBeOpened() {
        assertFalse(resource.isOpen());
    }

    @Test
    public void urlProtocolShouldMatch() throws Exception {
        assertEquals(getUrlProtocol(), resource.getURL().getProtocol());
    }

    public abstract String getUrlProtocol();

    @Test
    public void uriSchemeShouldMatch() throws Exception {
        assertEquals(getURIScheme(), resource.getURL().getProtocol());
    }

    public abstract String getURIScheme();

    @Test
    public abstract void shouldGetFile() throws Exception;

    @Test
    public void contentLength() throws Exception {
        // When
        try (OutputStream outputStream = resource.getOutputStream()) {
            outputStream.write("test".getBytes());
        }

        // Then
        assertEquals(4, resolver.getResource(LOCATION).contentLength());
    }

    @Test
    public abstract void lastModifiedShouldBeComputed() throws Exception;

    // TODO S3PathMatchingResourcePatternResolver doesn't handle createRealtive right now
    // @Test
    // public void createRelative() throws Exception {
    // // When
    // final DeletableResource root = resolver.getResource("/test/");
    // final DeletableResource relative = (DeletableResource) root.createRelative("side.txt");
    // try (OutputStream outputStream = relative.getOutputStream()) {
    // outputStream.write("test".getBytes());
    // }
    //
    // // Then
    // final DeletableResource side = resolver.getResource("/test/side.txt");
    // assertTrue(side.exists());
    // }

    @Test
    public abstract void getFilename() throws Exception;

    @Test
    public abstract void shouldGetDescription() throws Exception;

    @Test
    public void getInputStream() throws Exception {
        assertEquals("test", IOUtils.toString(resolver.getResource(LOCATION).getInputStream()));
    }

    @Test
    public void isWritable() {
        assertTrue(resource.isWritable());
    }

    @Test
    public void getOutputStream() throws Exception {
        try (OutputStream outputStream = resource.getOutputStream()) {
            assertNotNull(outputStream);
        }
    }

}
