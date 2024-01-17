// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.signature.exceptions;

// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com

public class VerifyException extends Exception {

    private static final long serialVersionUID = 1L;

    public VerifyException(String message) {
        super(message);
    }

    public VerifyException(String message, Throwable cause) {
        super(message, cause);
    }
}
