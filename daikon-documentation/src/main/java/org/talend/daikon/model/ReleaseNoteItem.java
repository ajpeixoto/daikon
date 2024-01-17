// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.model;

import java.io.PrintWriter;

public interface ReleaseNoteItem {

    ReleaseNoteItemType getIssueType();

    void writeTo(PrintWriter writer);

}
