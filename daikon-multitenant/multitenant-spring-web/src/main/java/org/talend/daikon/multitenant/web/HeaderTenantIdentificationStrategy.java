// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.multitenant.web;

import jakarta.servlet.http.HttpServletRequest;

/**
 * A {@link TenantIdentificationStrategy strategy} which looks for tenant identity from a given request header.
 * 
 * @author Clint Morgan (Tasktop Technologies Inc.)
 * 
 */
public class HeaderTenantIdentificationStrategy implements TenantIdentificationStrategy {

    private String headerName;

    @Override
    public Object identifyTenant(HttpServletRequest request) {
        return request.getHeader(headerName);
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

}
