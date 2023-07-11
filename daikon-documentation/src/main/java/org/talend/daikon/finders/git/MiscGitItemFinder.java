package org.talend.daikon.finders.git;

import java.util.regex.Matcher;
import java.util.stream.Stream;

import org.talend.daikon.finders.ItemFinder;
import org.talend.daikon.model.GitCommit;
import org.talend.daikon.model.MiscReleaseNoteItem;
import org.talend.daikon.model.ReleaseNoteItem;

/**
 * Finds Git commit for release notes <b>NOT</b> linked to any Jira.
 */
public class MiscGitItemFinder extends AbstractGitItemFinder implements ItemFinder {

    public MiscGitItemFinder(String version, String gitHubRepositoryUrl) {
        this(null, version, gitHubRepositoryUrl);
    }

    public MiscGitItemFinder(String pathname, String version, String gitHubRepositoryUrl) {
        super(version, pathname, gitHubRepositoryUrl);
    }

    @Override
    public Stream<? extends ReleaseNoteItem> find() {
        try {
            return getGitCommits() //
                    .filter(c -> !c.commit().getShortMessage().contains("release")) //
                    .map(c -> new Tuple(JIRA_DETECTION_PATTERN.matcher(c.commit().getShortMessage()), c)) //
                    .filter(t -> !t.matcher().matches()) //
                    .map(t -> new MiscReleaseNoteItem(t.gitCommit()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // @copilot(100%)
    private static record Tuple(Matcher matcher, GitCommit gitCommit) {

    }
    // @copilot
}
