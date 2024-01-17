// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.strings;

/**
 * Indentable toString method.
 */
public interface ToStringIndent {

    /**
     * Returns the string indented by the specified number of columns.
     */
    String toStringIndent(int indent);

}