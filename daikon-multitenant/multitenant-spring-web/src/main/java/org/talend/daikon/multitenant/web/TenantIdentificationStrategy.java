// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.multitenant.web;

import jakarta.servlet.http.HttpServletRequest;

/**
 * A strategy for identifying tenants from a {@link HttpServletRequest}.
 * 
 * @author Clint Morgan (Tasktop Technologies Inc.)
 */
public interface TenantIdentificationStrategy {

    Object identifyTenant(HttpServletRequest request);
}
