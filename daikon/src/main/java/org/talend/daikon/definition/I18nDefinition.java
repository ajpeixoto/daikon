// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.definition;

import org.talend.daikon.NamedThing;
import org.talend.daikon.SimpleNamedThing;

/**
 * Base class for all definitions that require some standard way of setting their title and displayName.<br>
 * for displayName you shall provide a properties file with the key <b>definition.XXX.displayName</b> where XXX is the value
 * returned by {@link NamedThing#getName()}<br>
 * for title you shall provide a properties file with the key <b>definition.XXX.title</b> where XXX is the value returned by
 * {@link NamedThing#getName()}<br>
 */
public class I18nDefinition extends SimpleNamedThing {

    public static final String DEFINITION_I18N_PREFIX = "definition.";

    public I18nDefinition(String name) {
        super(name);
    }

    @Override
    public String getDisplayName() {
        return getName() != null ? getI18nMessage(DEFINITION_I18N_PREFIX + getName() + I18N_DISPLAY_NAME_SUFFIX) : "";
    }

    /**
     * return the I18N title matching the <b>definition.[name].title</b> key in the associated .properties message where [name] is
     * the value returned by {@link I18nDefinition#getName()}.
     * If no I18N was found then the {@link #getDisplayName()} is used is any is provided.
     */
    @Override
    public String getTitle() {
        String title = getName() != null ? getI18nMessage(DEFINITION_I18N_PREFIX + getName() + I18N_TITLE_NAME_SUFFIX) : "";
        if ("".equals(title) || title.startsWith(DEFINITION_I18N_PREFIX)) {
            String displayName = getDisplayName();
            if (!"".equals(displayName) && !displayName.startsWith(DEFINITION_I18N_PREFIX)) {
                title = displayName;
            } // else title is what was computed before.
        } // else title is provided so use it.
        return title;
    }

}
