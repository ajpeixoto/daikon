// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.runtime;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

import org.ops4j.pax.url.mvn.Handler;
import org.ops4j.pax.url.mvn.ServiceConstants;

public class MvnHandlerConfigForSpring {

    static {// install the mvn protocol handler if not already installed.
        try {
            new URL("mvn:foo/bar");
        } catch (MalformedURLException e) {
            // handles mvn local repository
            String mvnLocalRepo = System.getProperty("maven.repo.local");
            if (mvnLocalRepo != null) {
                System.setProperty("org.ops4j.pax.url.mvn.localRepository", mvnLocalRepo);
            }
            URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {

                @Override
                public URLStreamHandler createURLStreamHandler(String protocol) {
                    if (ServiceConstants.PROTOCOL.equals(protocol)) {
                        return new Handler();
                    } else {
                        return null;
                    }
                }
            });
        }
    }
}
