// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.serialize.jsonschema.mapping;

import org.talend.daikon.serialize.jsonschema.UiSchemaConstants;

/**
 * Mapper for simple widgets.
 */
public class WidgetMapper extends Mapper {

    public WidgetMapper(String uiType) {
        super(UiSchemaConstants.TAG_WIDGET, uiType);
    }

}