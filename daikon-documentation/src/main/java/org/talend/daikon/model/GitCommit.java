// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.model;

import org.eclipse.jgit.revwalk.RevCommit;

public record GitCommit(RevCommit commit, PullRequest pullRequest) {


}
