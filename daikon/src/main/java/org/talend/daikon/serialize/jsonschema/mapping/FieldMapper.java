// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.serialize.jsonschema.mapping;

import org.talend.daikon.serialize.jsonschema.UiSchemaConstants;

/**
 * Mapper for ui:field widgets.
 */
public class FieldMapper extends Mapper {

    public FieldMapper(String uiType) {
        super(UiSchemaConstants.TAG_CUSTOM_WIDGET, uiType);
    }

    public FieldMapper(String uiAttribute, String uiType) {
        super(uiAttribute, uiType);
    }

}