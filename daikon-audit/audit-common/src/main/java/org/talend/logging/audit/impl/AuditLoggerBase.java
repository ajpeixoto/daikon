// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.logging.audit.impl;

import org.talend.logging.audit.Context;
import org.talend.logging.audit.LogLevel;

/**
 *
 */
public interface AuditLoggerBase {

    void log(LogLevel level, String category, Context context, Throwable throwable, String message);
}
