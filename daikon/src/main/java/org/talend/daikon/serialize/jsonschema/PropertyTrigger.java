// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.serialize.jsonschema;

/**
 * List possible callbacks on a property. As it is destined only to be serialized, in lower case, even if it would be preferable
 * to have an
 * enum serializer on jackson and/or spring to serialize in lower case.
 */
public enum PropertyTrigger {
    VALIDATE,
    BEFORE_ACTIVE,
    BEFORE_PRESENT,
    AFTER,
    /** Custom tag. Represent the widget(normally a button) which can open a new form */
    SHOW_FORM
}
