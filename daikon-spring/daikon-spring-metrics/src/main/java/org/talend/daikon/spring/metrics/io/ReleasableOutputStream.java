// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.metrics.io;

import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReleasableOutputStream extends OutputStream {

    private static final Logger LOG = LoggerFactory.getLogger(ReleasableOutputStream.class);

    private final OutputStream delegate;

    private final Runnable onClose;

    private boolean isClosed;

    public ReleasableOutputStream(OutputStream delegate, Runnable onClose) {
        this.delegate = delegate;
        this.onClose = onClose;
    }

    @Override
    public void write(int b) throws IOException {
        try {
            delegate.write(b);
        } catch (IOException e) {
            safeClose();
            throw e;
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        try {
            delegate.write(b);
        } catch (IOException e) {
            safeClose();
            throw e;
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        try {
            delegate.write(b, off, len);
        } catch (IOException e) {
            safeClose();
            throw e;
        }
    }

    @Override
    public void flush() throws IOException {
        try {
            delegate.flush();
        } catch (IOException e) {
            safeClose();
            isClosed = true;
            throw e;
        }
    }

    @Override
    public void close() throws IOException {
        try {
            delegate.close();
        } finally {
            safeClose();
            isClosed = true;
        }
    }

    private synchronized void safeClose() {
        if (isClosed) {
            return;
        }
        try {
            LOG.debug("Safe close on stream using {}", onClose);
            onClose.run();
            isClosed = true;
        } catch (Exception e) {
            LOG.error("Unable to invoke onClose closure.", e);
        }
    }
}
