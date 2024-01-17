// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.logging.audit.impl.http;

import org.talend.logging.audit.AuditAppenderException;
import org.talend.logging.audit.LogAppenders;

/**
 *
 */
public class HttpAppenderException extends AuditAppenderException {

    public HttpAppenderException(String message) {
        super(LogAppenders.HTTP, message);
    }

    public HttpAppenderException(Throwable cause) {
        super(LogAppenders.HTTP, cause);
    }
}
