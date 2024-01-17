// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.audit.logs.service;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.talend.daikon.spring.audit.logs.api.GenerateAuditLog;
import org.talend.logging.audit.Context;

/**
 * Audit log sender interface
 */
public interface AuditLogSender {

    /**
     * Send the audit log from a context given as parameter
     *
     * @param context audit log context
     */
    void sendAuditLog(Context context);

    /**
     * Build a context from given parameters and send the corresponding audit log
     *
     * @param tenant
     * @param request HTTP request
     * @param requestBody request body if different than HTTP request content
     * @param responseCode response code
     * @param responseObject response body object
     * @param auditLogAnnotation generate audit log annotation containing basic properties
     */
    void sendAuditLog(String tenant, ServerHttpRequest request, String requestBody, int responseCode, String responseObject,
            GenerateAuditLog auditLogAnnotation);

}
