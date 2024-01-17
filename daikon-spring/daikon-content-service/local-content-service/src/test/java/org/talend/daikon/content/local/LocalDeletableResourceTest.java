// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.content.local;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.test.context.TestPropertySource;
import org.talend.daikon.content.DeletableResourceTest;

@TestPropertySource(properties = { "content-service.store=local", "content-service.store.local.path=${java.io.tmpdir}/dataprep" })
public class LocalDeletableResourceTest extends DeletableResourceTest {

    @Override
    public String getUrlProtocol() {
        return "file";
    }

    @Override
    public String getURIScheme() {
        return "file";
    }

    @Override
    public void shouldGetFile() throws Exception {
        assertNotNull(resource.getFile());
    }

    @Override
    public void lastModifiedShouldBeComputed() throws Exception {
        assertTrue(resource.lastModified() > 0);
    }

    @Override
    public void getFilename() {
        assertEquals(LOCATION, resource.getFilename());
    }

    @Override
    public void shouldGetDescription() {
        assertTrue(resource.getDescription().contains("file.txt"));
    }

}
