// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.signature.exceptions;

public class UnsignedArchiveException extends VerifyFailedException {

    private static final long serialVersionUID = 1L;

    public UnsignedArchiveException(String message) {
        super(message);
    }
}
