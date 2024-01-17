// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.visitor.record;

import org.talend.daikon.avro.visitor.path.TraversalPath;

/**
 * Wrapper for an Avro record field - this is necessary to implement the visitor pattern
 * because Avro records are not visitable.
 *
 * Wrappers implementations are meant to be immutable
 *
 * @param <T> underlying value type
 */
public interface VisitableStructure<T> {

    /**
     * Visitable implementation
     * 
     * @param visitor the visitor
     */
    void accept(RecordVisitor visitor);

    /**
     * @return the actual value this field contains
     */
    T getValue();

    /**
     * @return the path of the field
     */
    TraversalPath getPath();

}
