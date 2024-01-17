// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon;

import org.talend.daikon.i18n.Translatable;

/**
 * Something that has a name.
 */
public interface NamedThing extends Translatable {

    /* suffix used for i18N to compute displayName key */
    String I18N_DISPLAY_NAME_SUFFIX = ".displayName"; //$NON-NLS-1$

    /* suffix used for i18N to compute displayName key */
    String I18N_TITLE_NAME_SUFFIX = ".title"; //$NON-NLS-1$

    /**
     * This is a technical (non i18n) name that shall be unique to identify the thing
     */
    String getName();

    /**
     * This is the name that will be displayed to the user, this may be internationalized.
     */
    String getDisplayName();

    /**
     * A multiword title that describes the thing.
     */
    String getTitle();

}