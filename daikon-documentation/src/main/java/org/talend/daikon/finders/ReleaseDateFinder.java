// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.finders;

import java.util.Date;

public interface ReleaseDateFinder {

    /**
     * @return The {@link Date} of the release.
     */
    Date find();
}
