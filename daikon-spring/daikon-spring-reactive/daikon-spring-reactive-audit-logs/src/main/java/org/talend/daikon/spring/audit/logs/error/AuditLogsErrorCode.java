// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.audit.logs.error;

import java.util.Collection;

import org.talend.daikon.exception.error.DefaultErrorCode;
import org.talend.daikon.exception.error.ErrorCode;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Error codes for Properties
 */
public enum AuditLogsErrorCode implements ErrorCode {

    METHOD_NOT_HANDLED(HttpServletResponse.SC_INTERNAL_SERVER_ERROR),
    TENANT_UNAVAILABLE(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

    private final DefaultErrorCode errorCodeDelegate;

    AuditLogsErrorCode(int httpStatus) {
        this.errorCodeDelegate = new DefaultErrorCode(httpStatus);
    }

    @Override
    public String getProduct() {
        return errorCodeDelegate.getProduct();
    }

    @Override
    public String getGroup() {
        return errorCodeDelegate.getGroup();
    }

    @Override
    public int getHttpStatus() {
        return errorCodeDelegate.getHttpStatus();
    }

    @Override
    public Collection<String> getExpectedContextEntries() {
        return errorCodeDelegate.getExpectedContextEntries();
    }

    @Override
    public String getCode() {
        return toString();
    }
}
