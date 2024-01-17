// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.content;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

import java.util.Collection;

import org.talend.daikon.exception.error.DefaultErrorCode;
import org.talend.daikon.exception.error.ErrorCode;

/**
 * Error codes for Content management.
 */
public enum ContentErrorCode implements ErrorCode {

    UNABLE_TO_GET_CONTENT(SC_INTERNAL_SERVER_ERROR),
    UNABLE_TO_PUT_CONTENT(SC_INTERNAL_SERVER_ERROR),
    UNABLE_TO_DELETE_CONTENT(SC_INTERNAL_SERVER_ERROR),
    UNABLE_TO_CLEAR_CONTENT(SC_INTERNAL_SERVER_ERROR);

    private DefaultErrorCode errorCodeDelegate;

    ContentErrorCode(int httpStatus) {
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
