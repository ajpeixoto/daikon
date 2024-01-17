// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.logging.audit;

import java.util.Map;

/**
 * Represents context information for audit events to be passed as a parameter (as opposite to MDC which maintains a
 * static map).
 * The context is read-only.
 * <p>
 * An instance should be obtained using {@link ContextBuilder} factory. For example:
 * 
 * <pre>
 * Context ctx = ContextBuilder.create("user", "testuser").build();
 * AuditLoggerFactory.getAuditLogger().securityInfo(ctx, "User {user} has logged in");
 * </pre>
 *
 * @see ContextBuilder
 */
public interface Context extends Map<String, String> {

}
