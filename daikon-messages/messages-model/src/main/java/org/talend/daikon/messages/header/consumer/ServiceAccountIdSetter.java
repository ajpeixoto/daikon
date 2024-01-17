// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.header.consumer;

@FunctionalInterface
public interface ServiceAccountIdSetter {

    void setCurrentServiceAccountId(String serviceAccountId);
}
