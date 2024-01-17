// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

interface TestRepository extends MongoRepository<TestData, String> {

}
