// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.jira;

import org.codehaus.jettison.json.JSONException;
import org.talend.daikon.model.JiraIssue;
import org.talend.daikon.model.SearchResult;

import java.io.IOException;
import java.util.Set;

public interface JiraClient {

    SearchResult searchJql(String jql, Integer maxResults, Integer startAt, Set<String> fields);

    JiraIssue getIssue(String jiraId) throws JSONException, IOException;
}
