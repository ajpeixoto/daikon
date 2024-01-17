// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.content.journal;

import java.util.stream.Stream;

import org.talend.daikon.content.ResourceResolver;

public interface ResourceJournal {

    /**
     * Synchronize the content of this journal
     */
    void sync();

    Stream<String> matches(String pattern);

    void clear(String pattern);

    void add(String location);

    void remove(String location);

    void move(String source, String target);

    boolean exist(String location);

    /**
     * @return <code>true</code> if journal is ready for usage, <code>false</code> otherwise.
     */
    boolean ready();

    /**
     * Marks this journal as ready for use. After method completes, {@link #ready()} must return <code>true</code>.
     * 
     * @see #ready()
     * @see #invalidate()
     */
    void validate();

    /**
     * Marks this journal as incomplete thus <b>not</b> ready for use. After method completes, {@link #ready()} must
     * return <code>false</code>.
     * 
     * @see #ready()
     * @see #validate()
     */
    void invalidate();

    /**
     * Set the resource resolver to use to get the resource
     * 
     * @param resourceResolver
     */
    void setResourceResolver(ResourceResolver resourceResolver);
}
