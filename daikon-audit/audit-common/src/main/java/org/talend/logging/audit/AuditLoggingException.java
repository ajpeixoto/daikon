// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.logging.audit;

/**
 *
 */
public class AuditLoggingException extends RuntimeException {

    public AuditLoggingException() {
        super();
    }

    public AuditLoggingException(String message) {
        super(message);
    }

    public AuditLoggingException(Throwable cause) {
        super(cause);
    }

    public AuditLoggingException(String message, Throwable cause) {
        super(message, cause);
    }
}
