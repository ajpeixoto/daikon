package org.talend.daikon.finders.git;

import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.daikon.finders.ItemFinder;
import org.talend.daikon.jira.JiraClient;
import org.talend.daikon.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Finds Jira for release note based of Git history (finds Jira ids from Git commit messages).
 */
public class JiraGitItemFinder extends AbstractGitItemFinder implements ItemFinder {

    private static final Logger LOGGER = LoggerFactory.getLogger(JiraGitItemFinder.class);

    private final String jiraServerUrl;

    private final JiraClient client;

    public JiraGitItemFinder(String jiraServerUrl, JiraClient client, String version, String gitHubRepositoryUrl) {
        this(null, jiraServerUrl, client, version, gitHubRepositoryUrl);
    }

    public JiraGitItemFinder(String gitRepositoryPath, String jiraServerUrl, JiraClient client, String version,
            String gitHubRepositoryUrl) {
        super(version, gitRepositoryPath, gitHubRepositoryUrl);
        this.jiraServerUrl = jiraServerUrl;
        this.client = client;
    }

    @Override
    public Stream<? extends ReleaseNoteItem> find() {
        try {
            final Map<String, JiraIssue> issueCache = new HashMap<>();
            return supplyAsync(() -> { // Get all Jira id from commits
                return getGitCommits() //
                        .filter(c -> !c.commit().getShortMessage().contains("release")) //
                        .map(c -> new RawGitCommit(JIRA_DETECTION_PATTERN.matcher(c.commit().getShortMessage()), c.pullRequest(),
                                c.commit().getShortMessage())) //
                        .filter(rawGitCommit -> rawGitCommit.jiraIdMatcher().matches()) //
                        .collect(Collectors.toList());
            }) //
                    .thenApply(jiraIds -> { // Get all Jira issues in one call
                        if (!jiraIds.isEmpty()) {
                            final String idList = jiraIds.stream() //
                                    .map(s -> "\"" + s.jiraIdMatcher().group(1) + "\"") //
                                    .collect(Collectors.joining(", "));
                            final String jql = "id IN (" + idList + ")";
                            try {
                                final SearchResult results = client.searchJql(jql, jiraIds.size(), 0, null);
                                for (JiraIssue issue : results.issues()) {
                                    issueCache.put(issue.key(), issue);
                                }
                            } catch (Exception e) {
                                // Log a warning but proceed (see https://jira.talendforge.org/browse/TDKN-349)
                                LOGGER.warn("Error looking for issues from JIRA", e);
                            }

                        }
                        return jiraIds;
                    }) //
                    .thenApply(rawGitCommits -> { // Get Jira issue from previous "cache" to speed up Jira operations
                        return rawGitCommits.stream().map(rawGitCommit -> {
                            final String jiraId = rawGitCommit.jiraIdMatcher().group(1);
                            JiraIssue issue = issueCache.get(jiraId);
                            if (issue == null) {
                                try {
                                    // Issue can move to another id in Jira (issue id changed between git log and Jira)
                                    issue = client.getIssue(jiraId);
                                } catch (Exception e) {
                                    // Log a warning but proceed (see https://jira.talendforge.org/browse/TDKN-349)
                                    LOGGER.warn("Error looking for issues from JIRA", e);
                                }

                            }
                            return new ProcessedJiraTuple(issue, rawGitCommit.pullRequest(), rawGitCommit.shortMessage());
                        });
                    }) //
                    .thenApply(processedJiraTuples -> processedJiraTuples.map(tuple -> new JiraReleaseNoteItem(tuple.issue(), //
                            jiraServerUrl, //
                            tuple.pullRequest(), tuple.shortMessage())) //
                    ) //
                    .get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private record RawGitCommit(Matcher jiraIdMatcher, PullRequest pullRequest, String shortMessage) {

    }

    private record ProcessedJiraTuple(JiraIssue issue, PullRequest pullRequest, String shortMessage) {

    }
}
