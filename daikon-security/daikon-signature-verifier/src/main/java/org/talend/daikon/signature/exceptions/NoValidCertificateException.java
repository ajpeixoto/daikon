// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.signature.exceptions;

public class NoValidCertificateException extends VerifyFailedException {

    private static final long serialVersionUID = 1L;

    public NoValidCertificateException(String message) {
        super(message);
    }
}
