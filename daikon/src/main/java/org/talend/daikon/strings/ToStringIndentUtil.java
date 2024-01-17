// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.strings;

public class ToStringIndentUtil {

    public static String indentString(int indent) {
        if (indent < 0) {
            return "";
        }
        return "                                                                              ".substring(0, indent);
    }

}