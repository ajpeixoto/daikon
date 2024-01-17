// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.model;

import java.io.PrintWriter;

import org.eclipse.jgit.revwalk.RevCommit;


public record MiscReleaseNoteItem(GitCommit commit) implements ReleaseNoteItem {

    @Override
    public ReleaseNoteItemType getIssueType() {
        return ReleaseNoteItemType.MISC;
    }

    @Override
    public void writeTo(PrintWriter writer) {
        final PullRequest pullRequest = commit.pullRequest();
        final RevCommit commit = this.commit.commit();
        if (pullRequest != null) {
            final String processedShortMessage = commit.getShortMessage().replace("(#" + pullRequest.display() + ")", "");
            writer.println(
                    "- " + processedShortMessage + " (link:" + pullRequest.url() + "[#" + pullRequest.display() + "])");
        } else {
            writer.println("- " + commit.getShortMessage());
        }
    }

    @Override
    public String toString() {
        return "MiscReleaseNoteItem{" + getIssueType() + ", " + "commit=" + commit.commit().getShortMessage() + '}';
    }

}
