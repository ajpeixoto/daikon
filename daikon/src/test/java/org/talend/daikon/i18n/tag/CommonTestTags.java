// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.i18n.tag;

import org.talend.daikon.i18n.GlobalI18N;

/**
 * Tags used for testing purpose.
 */
public class CommonTestTags {

    public static final TagImpl COMMON_TAG = new TagImpl("commonTag", null,
            GlobalI18N.getI18nMessageProvider().getI18nMessages(CommonTestTags.class));

}
