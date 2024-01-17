// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.metrics.io;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.output.ProxyOutputStream;

import io.micrometer.tracing.annotation.NewSpan;

public class SpanOutputStream extends ProxyOutputStream {

    public SpanOutputStream(OutputStream delegate) {
        super(delegate);
    }

    @Override
    @NewSpan("metered-output-stream-flush")
    public void flush() throws IOException {
        super.flush();
    }

    @Override
    @NewSpan("metered-output-stream-close")
    public void close() throws IOException {
        super.close();
    }
}
