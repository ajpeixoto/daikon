// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.audit.logs.service;

import jakarta.servlet.http.HttpServletRequest;

public interface AuditLogUrlExtractor {

    String extract(HttpServletRequest servletRequest);
}
