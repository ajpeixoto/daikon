// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.i18n.tag;

import java.util.Collections;
import java.util.List;

import org.talend.daikon.i18n.TranslatableImpl;

/**
 * Implementation of HasTags interface with internationalization.
 */
public class TranslatableTaggedImpl extends TranslatableImpl implements HasTags {

    private List<TagImpl> tags;

    @Override
    public List<TagImpl> getTags() {
        if (tags == null) {
            tags = doGetTags();
            for (TagImpl tag : tags) {
                tag.setI18nMessageFormatter(getI18nMessageFormatter());
            }
        }
        return tags;
    }

    /**
     * Get tags. Override this method to implement tags support.
     */
    protected List<TagImpl> doGetTags() {
        return Collections.emptyList();
    }

}
