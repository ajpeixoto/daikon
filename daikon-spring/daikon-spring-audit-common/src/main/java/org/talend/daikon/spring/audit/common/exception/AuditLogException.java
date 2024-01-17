// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.audit.common.exception;

import org.talend.daikon.exception.ExceptionContext;
import org.talend.daikon.exception.TalendRuntimeException;
import org.talend.daikon.exception.error.ErrorCode;

/**
 * Class for audit log exceptions
 */
public class AuditLogException extends TalendRuntimeException {

    /**
     *
     * @param code code of the error
     * @param context context of the error when it occurred
     */
    public AuditLogException(ErrorCode code, ExceptionContext context) {
        super(code, context);
    }

    /**
     *
     * @param code code of the error
     * @param cause root cause of the error
     */
    public AuditLogException(ErrorCode code, Throwable cause) {
        super(code, cause);
    }
}
