// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.junit.jupiter.api.Test;

/**
 * created by wchen on Nov 15, 2016
 * Detailled comment
 */
public class SSLContextProviderTest {

    @Test
    public void testBuildContext() throws UnrecoverableKeyException, KeyManagementException, KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException, URISyntaxException {
        URL keyStore = this.getClass().getResource("keystore");
        String keyPath = new File(keyStore.toURI().getSchemeSpecificPart()).getAbsolutePath();
        URL trustStore = this.getClass().getResource("truststore");
        String trustPath = new File(trustStore.toURI().getSchemeSpecificPart()).getAbsolutePath();
        SSLContext buildContext = SSLContextProvider.buildContext("TLS", keyPath, "talend", "JKS", trustPath, "talend", "JKS");
        assertNotNull(buildContext);
    }
}
