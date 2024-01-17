// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.visitor.record;

import org.talend.daikon.avro.visitor.path.TraversalPath;

/**
 * Abstract base implementation of {@link VisitableStructure}.
 *
 * This implementation is immutable, constructor's arguments are final members.
 *
 * @param <T> the inner type of value
 */
abstract class AbstractVisitableStructure<T> implements VisitableStructure<T> {

    private final T value;

    private final TraversalPath path;

    protected AbstractVisitableStructure(T value, TraversalPath path) {
        this.value = value;
        this.path = path;
    }

    @Override
    public final T getValue() {
        return value;
    }

    @Override
    public TraversalPath getPath() {
        return path;
    }
}
