// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.audit.common.api;

import java.util.Arrays;

/**
 * Audit log scope indicating if audit logs must be generated for :
 * - ALL cases
 * - SUCCESS cases only
 * - ERROR cases only
 */
public enum AuditLogScope {
    ALL,
    SUCCESS,
    ERROR;

    public boolean in(AuditLogScope... scopes) {
        return Arrays.stream(scopes).anyMatch(scope -> scope == this);
    }
}
