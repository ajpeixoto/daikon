// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.header.producer;

/**
 * Provide current service information
 */
public interface ServiceInfoProvider {

    /**
     * @return the name of the current service
     */
    String getServiceName();

    /**
     * @return the version of the current service
     */
    String getServiceVersion();

    /**
     * @return the name of the current application
     */
    String getApplicationName();

}
