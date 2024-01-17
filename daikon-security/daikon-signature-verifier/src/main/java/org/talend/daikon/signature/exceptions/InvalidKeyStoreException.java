// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.signature.exceptions;

public class InvalidKeyStoreException extends VerifyException {

    private static final long serialVersionUID = 1L;

    public InvalidKeyStoreException(String message) {
        super(message);
    }

    public InvalidKeyStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
