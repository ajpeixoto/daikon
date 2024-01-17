// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.exception.error;

import java.io.Serializable;
import java.util.Collection;

/**
 * An error code uniquely describes an error condition (for reference, documentation, UI, etc).
 */
public interface ErrorCode extends Serializable {

    /**
     * @return the product used for the error message... Hum... TDP ? ;-)
     */
    String getProduct();

    /**
     * @return the group this message belongs to (API, DATASET, PREPARATION...)
     */
    String getGroup();

    /**
     * @return the http status to return.
     */
    int getHttpStatus();

    /**
     * @return the expected context entries if any.
     */
    Collection<String> getExpectedContextEntries();

    /**
     * @return the full code for this message.
     */
    String getCode();

}
