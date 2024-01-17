// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.header.consumer;

/**
 * Created by bguillon on 07/11/2017.
 */
@FunctionalInterface
public interface UserIdSetter {

    void setCurrentUserId(String userId);

}
