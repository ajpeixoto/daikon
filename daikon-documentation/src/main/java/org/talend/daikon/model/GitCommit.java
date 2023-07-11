package org.talend.daikon.model;

import org.eclipse.jgit.revwalk.RevCommit;

public record GitCommit(RevCommit commit, PullRequest pullRequest) {


}
