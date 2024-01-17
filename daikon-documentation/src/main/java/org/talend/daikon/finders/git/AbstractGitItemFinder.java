// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.finders.git;

import static java.util.Comparator.comparing;
import static org.eclipse.jgit.lib.Constants.R_TAGS;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefDatabase;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.talend.daikon.model.GitCommit;
import org.talend.daikon.model.PullRequest;

import com.google.common.collect.Streams;

public abstract class AbstractGitItemFinder {

    static final Pattern JIRA_DETECTION_PATTERN = Pattern.compile(".*((?<!([A-Z]{1,10})-?)[A-Z]+-\\d+).*");

    private static final Pattern PULL_REQUEST_PATTERN = Pattern.compile(".*#(\\d+).*");

    private final String gitHubRepositoryUrl;

    private final String version;

    private final String gitRepositoryPath;

    AbstractGitItemFinder(String version, String gitRepositoryPath, String gitHubRepositoryUrl) {
        this.version = version;
        this.gitRepositoryPath = gitRepositoryPath;
        this.gitHubRepositoryUrl = gitHubRepositoryUrl;
    }

    private static Date getDate(RevWalk walk, Ref ref) {
        try {
            return walk.parseTag(ref.getObjectId()).getTaggerIdent().getWhen();
        } catch (IOException e) {
            return new Date(0);
        }
    }

    private static ObjectId getTagCommitId(RevWalk walk, Ref t) {
        try {
            return walk.parseTag(t.getObjectId()).getObject().getId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PullRequest getPullRequestLink(RevCommit commit) {
        final Matcher matcher = PULL_REQUEST_PATTERN.matcher(commit.getShortMessage());
        return matcher.matches() ? new PullRequest(gitHubRepositoryUrl + "/pull/" + matcher.group(1), matcher.group(1)) : null;
    }

    Stream<GitCommit> getGitCommits() {
        try {
            // Init git client
            final File dir;
            if (StringUtils.isNotBlank(gitRepositoryPath)) {
                dir = new File(gitRepositoryPath);
            } else {
                dir = new File(".");
            }
            final Git git = Git.open(dir);

            final Repository repository = git.getRepository();
            final RefDatabase refDatabase = repository.getRefDatabase();
            final RevWalk walk = new RevWalk(repository);
            if (refDatabase.hasRefs()) {
                final GitRange refsByPrefix = findRange(refDatabase, walk, version);
                final ObjectId start = refsByPrefix.start;
                final ObjectId head = refsByPrefix.end;
                return Streams.stream(git.log().addRange(start, head).call()) //
                        .map(commit -> new GitCommit(commit, getPullRequestLink(commit)));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Stream.empty();
    }

    private GitRange findRange(RefDatabase refDatabase, RevWalk walk, String version) throws IOException {
        final ObjectId head = refDatabase.findRef("HEAD").getObjectId();

        if (StringUtils.isBlank(version)) {
            return fullHistory(walk, refDatabase.getRefsByPrefix(R_TAGS).stream(), head);
        } else {
            final AtomicBoolean hasMetVersion = new AtomicBoolean(false);
            final ThreadLocal<ObjectId> start = new ThreadLocal<>();

            final Optional<ObjectId> end = refDatabase.getRefsByPrefix(R_TAGS).stream() //
                    .sorted((r1, r2) -> getDate(walk, r2).compareTo(getDate(walk, r1))) //
                    .filter(t -> {
                        if (!hasMetVersion.get()) {
                            if (t.getName().contains(version)) {
                                hasMetVersion.set(true);
                                start.set(getTagCommitId(walk, t));
                                return false; // So jump to next element after this one.
                            }
                        }
                        return hasMetVersion.get();
                    }) //
                    .findFirst() //
                    .map(ref -> getTagCommitId(walk, ref));

            if (start.get() == null) {
                // Unable to find the version in tags... use the latest created tag as start
                final Optional<ObjectId> firstTag = refDatabase.getRefsByPrefix(R_TAGS).stream() //
                        .min((r1, r2) -> getDate(walk, r2).compareTo(getDate(walk, r1))) //
                        .map(ref -> getTagCommitId(walk, ref));
                firstTag.ifPresent(start::set);
            }

            if (start.get() == null) {
                return fullHistory(walk, refDatabase.getRefsByPrefix(R_TAGS).stream(), head);
            } else {
                return new GitRange(start.get(), end.map(ObjectId::toObjectId).orElse(head));
            }
        }
    }

    private GitRange fullHistory(RevWalk walk, Stream<Ref> base, ObjectId head) {
        return base.max(comparing(r -> getDate(walk, r))) // Get latest tag from history
                .map(start -> new GitRange(start.getObjectId(), head)) // If found, range from latest to HEAD
                .orElseGet(() -> { // Or else return range from HEAD to root
                    try {
                        RevCommit startCommit = walk.lookupCommit(head);
                        walk.markStart(startCommit);
                        for (RevCommit revCommit : walk) {
                            startCommit = revCommit;
                        }
                        return new GitRange(startCommit, head);
                    } catch (IOException e) {
                        throw new RuntimeException("Unable to find start commit.", e);
                    }
                });
    }

    static record GitRange(ObjectId start, ObjectId end) {

    }
}
