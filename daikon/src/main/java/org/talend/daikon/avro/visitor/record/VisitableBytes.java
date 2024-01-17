// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.visitor.record;

import java.nio.ByteBuffer;

import org.talend.daikon.avro.visitor.path.TraversalPath;

/**
 * Wrapper implementation for Bytes Avro type
 */
public class VisitableBytes extends AbstractVisitableStructure<ByteBuffer> {

    VisitableBytes(ByteBuffer value, TraversalPath path) {
        super(value, path);
    }

    @Override
    public void accept(RecordVisitor visitor) {
        visitor.visit(this);

    }
}
