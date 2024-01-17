// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties.error;

import java.util.Collection;

import org.talend.daikon.exception.error.DefaultErrorCode;
import org.talend.daikon.exception.error.ErrorCode;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Error codes for Properties
 */
public enum PropertiesErrorCode implements ErrorCode {

    MISSING_I18N_TRANSLATOR(
            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            "key", //$NON-NLS-1$
            "baseName"), //$NON-NLS-1$
    PROPERTIES_HAS_UNITIALIZED_PROPS(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "name", "field"),
    FAILED_INVOKE_METHOD(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "class", "method");

    private DefaultErrorCode errorCodeDelegate;

    PropertiesErrorCode(int httpStatus) {
        this.errorCodeDelegate = new DefaultErrorCode(httpStatus);
    }

    PropertiesErrorCode(int httpStatus, String... contextEntries) {
        this.errorCodeDelegate = new DefaultErrorCode(httpStatus, contextEntries);
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
