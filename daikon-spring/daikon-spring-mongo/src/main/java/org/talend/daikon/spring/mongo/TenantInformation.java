// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.mongo;

import com.mongodb.MongoClientSettings;

public interface TenantInformation {

    String getDatabaseName();

    MongoClientSettings getClientSettings();
}
