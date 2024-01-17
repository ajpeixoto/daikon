// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.mongo;

import java.io.Closeable;

import com.mongodb.reactivestreams.client.MongoClient;

public interface ReactiveMongoClientProvider extends Closeable {

    MongoClient get(TenantInformation tenantInformation);

    void close(TenantInformation tenantInformation);
}
