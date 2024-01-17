// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.i18n.tag;

import java.util.List;

/**
 * Interface for tags support.
 */
public interface HasTags {

    /**
     * Get declared tags.
     */
    List<? extends Tag> getTags();

}
