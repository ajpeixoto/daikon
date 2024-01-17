// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.model;

import java.io.PrintWriter;


public record JiraReleaseNoteItem(JiraIssue issue, String jiraServerUrl, PullRequest pullRequest,
                                  String shortMessage) implements ReleaseNoteItem {

    @Override
    public ReleaseNoteItemType getIssueType() {
        if (issue != null) {
            return ReleaseNoteItemType.fromJiraIssueType(issue.issueType());
        }
        return ReleaseNoteItemType.MISC;
    }

    @Override
    public void writeTo(PrintWriter writer) {
        if (issue != null) {
            writer.print(
                    "- link:" + jiraServerUrl + "/browse/" + issue.key() + "[" + issue.key() + "]: " + issue.summary());
        } else if (shortMessage != null) {
            writer.print("- " + shortMessage);
        }
        if (pullRequest != null) {
            writer.println(" (link:" + pullRequest.url() + "[#" + pullRequest.display() + "])");
        } else {
            writer.println();
        }
    }

    @Override
    public String toString() {
        String description = "JiraReleaseNoteItem{" + getIssueType();
        if (issue != null) {
            description += ", " + "issue=" + issue.summary() + '}';
        } else if (shortMessage != null) {
            description += ", " + "commit=" + shortMessage + '}';
        }
        return description;
    }

}
