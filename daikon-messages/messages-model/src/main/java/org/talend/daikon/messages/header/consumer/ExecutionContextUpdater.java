// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.header.consumer;

import org.apache.avro.generic.IndexedRecord;

public interface ExecutionContextUpdater {

    void updateExecutionContext(IndexedRecord message);

}
