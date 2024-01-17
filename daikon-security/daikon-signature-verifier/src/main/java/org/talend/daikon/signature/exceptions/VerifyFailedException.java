// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.signature.exceptions;

public class VerifyFailedException extends VerifyException {

    private static final long serialVersionUID = 1L;

    public VerifyFailedException(String message) {
        super(message);
    }

    public VerifyFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
