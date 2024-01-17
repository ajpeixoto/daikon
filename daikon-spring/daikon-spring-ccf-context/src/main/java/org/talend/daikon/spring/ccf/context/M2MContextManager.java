// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.ccf.context;

import java.util.Optional;

import org.talend.iam.scim.model.User;

public interface M2MContextManager {

    void clearContext();

    void injectContext(String tenantId, String userId, Optional<User> user);
}
