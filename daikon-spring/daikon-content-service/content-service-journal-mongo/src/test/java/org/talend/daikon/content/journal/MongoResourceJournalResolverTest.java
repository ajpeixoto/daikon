// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.content.journal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.talend.daikon.content.DeletableResource;
import org.talend.daikon.content.ResourceResolver;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@Testcontainers
public class MongoResourceJournalResolverTest extends MongoDBConfiguration {

    /**
     * Resource resolver use to get the resource
     */
    @Autowired
    private ResourceResolver resourceResolver;

    @Autowired
    private MongoResourceJournalResolver resolver;

    /**
     * Spring MongoDB template.
     */
    @Autowired
    private MongoResourceJournalRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${mongo.resource.journal.collection:resourceJournalEntry}")
    private String collectionName;

    @Test
    public void testClear() {

        resolver.clear("/location1");

        assertEquals(0L, repository.countByName("/location1.1"), "Location 1.1 should not exist anymore");
        assertEquals(0L, repository.countByName("/location1.2"), "Location 1.2 should not exist anymore");
        assertEquals(0L, repository.countByName("/location1.3"), "Location 1.3 should not exist anymore");
        assertEquals(1L, repository.countByName("/location2.1"), "Location 2.1 should still exist");
        assertEquals(1L, repository.countByName("/location2.2"), "Location 2.3 should still exist");
    }

    @BeforeEach
    public void initData() {
        resolver.setResourceResolver(resourceResolver);

        mongoTemplate.remove(new Query(), ResourceJournalEntry.class, collectionName);
        resolver.add("notlocation1.1");
        resolver.add("location1.1");
        resolver.add("location1.2");
        resolver.add("location1.3");
        resolver.add("location2.1");
        resolver.add("location2.2");

        Mockito.reset(resourceResolver);
    }

    @AfterEach
    public void cleanData() {
        mongoTemplate.remove(new Query(), ResourceJournalEntry.class, collectionName);
    }

    @Test
    public void testContext() {
        assertNotNull(resolver);
        assertNotNull(repository);
    }

    @Test
    public void testStartWith() {
        List<ResourceJournalEntry> listLocation = repository.findByNameStartsWith("/location1");

        assertEquals(3, listLocation.size());
        for (ResourceJournalEntry resourceJournalEntry : listLocation) {
            assertTrue(resourceJournalEntry.getName().startsWith("/location1"));
        }
    }

    @Test
    public void testExist() {
        // Then
        assertTrue(resolver.exist("/location1.1"));
        assertFalse(resolver.exist("/location1.5"));
    }

    @Test
    public void testMatches() {
        // When
        List<String> listLocation = resolver.matches("/location1*").collect(Collectors.toList());

        // Then
        assertEquals(3, listLocation.size(), "Location 2.3 should still exist");
        for (String location : listLocation) {
            assertTrue(location.startsWith("/location1"), "Location should start by location1");
        }
    }

    @Test
    public void testClearWithPattern() {
        // Given
        resolver.add("location3.0");
        resolver.add("location3.0/location3.1");
        resolver.add("location3.0/location3.2/location3.2.1");
        resolver.add("location3.0/location3.3/location3.3.1");

        assertTrue(resolver.exist("location3.0"));
        assertTrue(resolver.exist("location3.0/location3.3/location3.3.1"));

        resolver.clear("location3.0/**");

        assertTrue(resolver.exist("location3.0"));
        assertFalse(resolver.exist("location3.0/location3.2/location3.2.1"));
        assertFalse(resolver.exist("location3.0/location3.3/location3.3.1"));

    }

    @Test
    public void testAdd() {
        // Given
        long nbLocation = countRecord();
        resolver.add("location3.0");

        // When
        final long count = countRecord();

        // Then
        assertEquals(nbLocation + 1, count, "Nb location should be equals");
    }

    @Test
    public void testRemove() {
        // Given
        long nbLocation = countRecord();
        resolver.remove("/location2.2");

        // When
        final long count = countRecord();

        // Then
        assertEquals(nbLocation - 1, count, "Nb location should be equals");
    }

    @Test
    public void testMove() {
        // Given
        assertTrue(resolver.exist("/location1.1"));
        assertFalse(resolver.exist("/location1.5"));

        // When
        resolver.move("/location1.1", "/location1.5");

        // Then
        assertTrue(resolver.exist("/location1.5"));
        assertFalse(resolver.exist("/location1.1"));
    }

    @Test
    public void shouldNotFailMoveWhenResourceDoesNotExist() {
        // When
        resolver.move("/does.not.exist", "/location1.5");

        // Then
        assertFalse(resolver.exist("/does.not.exist"));
        assertFalse(resolver.exist("/location1.5"));
    }

    @Test
    public void shouldNotBeReadyIfMarkerDoesNotExist() {
        // When
        final boolean ready = resolver.ready();

        // Then
        assertFalse(ready);
    }

    @Test
    public void shouldBeReadyIfMarkerExists() {
        // Given
        repository.save(new ResourceJournalEntry(MongoResourceJournalResolver.JOURNAL_READY_MARKER));

        // When
        final boolean ready = resolver.ready();

        // Then
        assertTrue(ready);
    }

    @Test
    public void shouldValidateCreateMarkDocument() {
        // When
        resolver.validate();

        // Then
        assertTrue(repository.exists(MongoResourceJournalResolver.JOURNAL_READY_MARKER));
    }

    @Test
    public void shouldInvalidateDeleteMarkDocument() {
        // When
        resolver.validate();

        // Then
        assertTrue(repository.exists(MongoResourceJournalResolver.JOURNAL_READY_MARKER));

        // When
        resolver.invalidate();

        // Then
        assertFalse(repository.exists(MongoResourceJournalResolver.JOURNAL_READY_MARKER));
    }

    @Test
    public void shouldSyncWithResourceResolver() throws IOException {
        // Given
        final DeletableResource resource1 = mock(DeletableResource.class);
        final DeletableResource resource2 = mock(DeletableResource.class);
        when(resourceResolver.getResources(any())).thenReturn(new DeletableResource[] { resource1, resource2 });
        when(resourceResolver.getLocationPrefix()).thenReturn("");

        when(resource1.getAbsolutePath()).thenReturn("resource1");
        when(resource2.getAbsolutePath()).thenReturn("resource2");

        // When
        resolver.sync();

        // Then
        verify(resourceResolver, times(1)).getResources(eq("/**"));
        verify(resource1, times(1)).getAbsolutePath();
        verify(resource2, times(1)).getAbsolutePath();
        assertTrue(repository.exists(MongoResourceJournalResolver.JOURNAL_READY_MARKER));
    }

    @Test
    public void shouldSyncWithResourceResolverAndPrefix1() throws IOException, InterruptedException {
        // Given
        final DeletableResource resource1 = mock(DeletableResource.class);
        final DeletableResource resource2 = mock(DeletableResource.class);
        final DeletableResource resource3 = mock(DeletableResource.class);
        final DeletableResource resource4 = mock(DeletableResource.class);
        when(resourceResolver.getResources(any()))
                .thenReturn(new DeletableResource[] { resource1, resource2, resource3, resource4 });
        when(resourceResolver.getLocationPrefix()).thenReturn("prefix");

        when(resource1.getAbsolutePath()).thenReturn("/prefix/resource1");
        when(resource2.getAbsolutePath()).thenReturn("prefix/resource2");
        when(resource3.getAbsolutePath()).thenReturn("/unprefix/resource3");
        when(resource4.getAbsolutePath()).thenReturn("unprefix/resource4");

        // When
        resolver.sync();

        // Then
        assertTrue(resolver.exist("/resource1"));
        assertTrue(resolver.exist("/resource2"));
        assertTrue(resolver.exist("/unprefix/resource3"));
        assertTrue(resolver.exist("/unprefix/resource4"));
    }

    @Test
    public void shouldSyncWithResourceResolverAndPrefix2() throws IOException, InterruptedException {
        // Given
        final DeletableResource resource1 = mock(DeletableResource.class);
        final DeletableResource resource2 = mock(DeletableResource.class);
        final DeletableResource resource3 = mock(DeletableResource.class);
        final DeletableResource resource4 = mock(DeletableResource.class);
        when(resourceResolver.getResources(any()))
                .thenReturn(new DeletableResource[] { resource1, resource2, resource3, resource4 });
        when(resourceResolver.getLocationPrefix()).thenReturn("/prefix");

        when(resource1.getAbsolutePath()).thenReturn("/prefix/resource1");
        when(resource2.getAbsolutePath()).thenReturn("prefix/resource2");
        when(resource3.getAbsolutePath()).thenReturn("/unprefix/resource3");
        when(resource4.getAbsolutePath()).thenReturn("unprefix/resource4");

        // When
        resolver.sync();

        // Then
        assertTrue(resolver.exist("/resource1"));
        assertTrue(resolver.exist("/resource2"));
        assertTrue(resolver.exist("/unprefix/resource3"));
        assertTrue(resolver.exist("/unprefix/resource4"));
    }

    @Test
    public void shouldNotMarkAsReadyWhenSyncFails() throws IOException, InterruptedException {
        // Given
        when(resourceResolver.getResources(any())).thenThrow(new IOException("Unchecked on purpose"));

        // When
        try {
            resolver.sync();
            fail("Expected an exception.");
        } catch (Exception e) {
            // Expected
        }

        // Then
        assertFalse(repository.exists(MongoResourceJournalResolver.JOURNAL_READY_MARKER));
    }

    @Test
    public void shouldIgnoreIfAlreadyMarkedAsReady() throws IOException {
        // Given
        when(resourceResolver.getResources(any())).thenThrow(new IOException("Unchecked on purpose"));

        // When
        resolver.validate();
        resolver.sync();

        // Then
        verify(resourceResolver, never()).getResources(eq("/**"));
    }

    @Test
    public void shouldMatchIgnoringAbsolutePath() {
        // When
        final Stream<String> matches = resolver.matches("/location1.1");

        // Then
        assertEquals(1, matches.count());
    }

    @Test
    public void shouldMatchIgnoringEmptyPath() {
        // When
        final Stream<String> matches = resolver.matches(null);

        // Then
        assertEquals(0, matches.count());
    }

    private long countRecord() {
        return mongoTemplate.count(new Query(), ResourceJournalEntry.class, collectionName);
    }

    @AutoConfiguration
    @ComponentScan("org.talend.daikon.content.journal")
    public static class SpringConfig {
    }

}
