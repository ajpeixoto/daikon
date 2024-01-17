// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.model;

public enum ReleaseNoteItemType {

    BUG("Fix"),
    FEATURE("Feature"),
    WORK_ITEM("Work Item"),
    MISC("Other");

    private final String displayName;

    ReleaseNoteItemType(String displayName) {
        this.displayName = displayName;
    }

    public static ReleaseNoteItemType fromJiraIssueType(String issueType) {
        switch (issueType.toLowerCase()) {
        case "bug":
            return BUG;
        case "work item":
            return WORK_ITEM;
        case "new feature":
        case "epic":
            return FEATURE;
        default:
            return MISC;
        }
    }

    public String getDisplayName() {
        return displayName;
    }
}
