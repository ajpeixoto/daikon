// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.finders;

import java.util.stream.Stream;

import org.talend.daikon.model.Author;

public interface AuthorFinder {

    Stream<Author> findAuthors();
}
