// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.sandbox;

/**
 * Provide finer-grained control for the creation of {@link SandboxedInstance}s.
 *
 * A {@link org.talend.daikon.runtime.RuntimeInfo} can also optionally implement this class to guide the creation of
 * runtime instances.
 */
public interface SandboxControl {

    /**
     * Constant, which specifies ClassLoader is reusable.
     * If ClassLoader is not reusable then
     * 
     * <pre>
     * !CLASSLOADER_REUSABLE
     * </pre>
     * 
     * may be used
     */
    static final boolean CLASSLOADER_REUSABLE = true;

    /**
     * Most {@link org.talend.daikon.runtime.RuntimeInfo} instances can reuse the classloader that dynamically adds
     * dependencies to the classpath. This can largely improve performance. However, in certain cases, the loaded
     * classes retain state between calls and should not be reused (notably when static members of loaded classes
     * contain state information, and can't be reset within the runtime instance). In this case, this method can be
     * overridden to determine whether the ClassLoader can be obtained from the cache (if available) and saved in the
     * cache for future use.
     * 
     * @return true if the ClassLoader can be cached for future reuse. If false, a new ClassLoader should be created and
     * never reused for future runtime instances.
     */
    boolean isClassLoaderReusable();
}
