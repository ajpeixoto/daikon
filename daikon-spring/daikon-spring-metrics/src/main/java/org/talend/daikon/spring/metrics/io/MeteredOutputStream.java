// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.metrics.io;

import static org.talend.daikon.spring.metrics.io.Metered.Type.OUT;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.output.ProxyOutputStream;

public class MeteredOutputStream extends ProxyOutputStream implements Metered {

    private final OutputStream delegate;

    private long volume;

    public MeteredOutputStream(OutputStream delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    @Override
    public void write(int b) throws IOException {
        try {
            delegate.write(b);
        } finally {
            volume++;
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        try {
            delegate.write(b);
        } finally {
            volume += b.length;
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        try {
            delegate.write(b, off, len);
        } finally {
            volume += len;
        }
    }

    @Override
    public long getVolume() {
        return volume;
    }

    @Override
    public Type getType() {
        return OUT;
    }
}
