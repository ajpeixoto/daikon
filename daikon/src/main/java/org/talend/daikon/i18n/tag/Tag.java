// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.i18n.tag;

/**
 * Interface for Tag presentation.
 */
public interface Tag {

    public static final String TAG_PREFIX = "tag.";

    /**
     * @return parent tag if present or return null
     */
    public Tag getParentTag();

    /**
     * Get translated current tag value.
     */
    public String getTranslatedValue();

    /**
     * Get current tag value.
     */
    public String getValue();

}
