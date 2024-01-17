// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.multitenant.async;

/**
 * Allows to interact with {@link ContextAwareCallable} to propagate any context specific information from a thread to another
 */
public interface ContextPropagator {

    /**
     * Called before switch thread
     */
    void captureContext();

    /**
     * Called on the new thread
     */
    void setupContext();

    /**
     * Called at the end of the execution.
     */
    void restoreContext();
}
