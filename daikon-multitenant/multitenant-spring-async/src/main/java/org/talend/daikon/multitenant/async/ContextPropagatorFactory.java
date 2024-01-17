// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.multitenant.async;

import java.util.function.Supplier;

/**
 * Factory of {@link ContextPropagator}
 */
public interface ContextPropagatorFactory extends Supplier<ContextPropagator> {

}
