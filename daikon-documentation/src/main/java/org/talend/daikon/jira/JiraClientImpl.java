package org.talend.daikon.jira;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.UriBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.talend.daikon.model.JiraIssue;
import org.talend.daikon.model.SearchResult;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

public class JiraClientImpl implements JiraClient {

    private static final String START_AT_ATTRIBUTE = "startAt";

    private static final String MAX_RESULTS_ATTRIBUTE = "maxResults";

    private static final int MAX_JQL_LENGTH_FOR_HTTP_GET = 500;

    private static final String JQL_ATTRIBUTE = "jql";

    private static final String FILTER_FAVOURITE_PATH = "filter/favourite";

    private static final String FILTER_PATH_FORMAT = "filter/%s";

    private static final String SEARCH_URI_PREFIX = "/rest/api/2/search";

    private static final String EXPAND_ATTRIBUTE = "expand";

    private static final String FIELDS_ATTRIBUTE = "fields";

    private static final Function<? super Expandos, String> EXPANDO_TO_PARAM = expandos -> expandos.name().toLowerCase();

    private final URI searchUri;

    private final URI favouriteUri;

    private final URI baseUri;

    private final BasicCredentialsProvider credsProvider;

    public JiraClientImpl(final URI baseUri, String user, String password) throws URISyntaxException {
        this.baseUri = baseUri;
        this.searchUri = UriBuilder.fromUri(baseUri).path(SEARCH_URI_PREFIX).build();
        this.favouriteUri = UriBuilder.fromUri(baseUri).path(FILTER_FAVOURITE_PATH).build();

        credsProvider = new BasicCredentialsProvider();
        AuthScope authScope = new AuthScope(HttpHost.create(baseUri.toString()));
        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(user, password.toCharArray()));
    }

    @Override
    public SearchResult searchJql(String jql, Integer maxResults, Integer startAt, Set<String> fields) {
        // call the Jira rest API to get the issue and map the restul into a SearchResult
        final Iterable<String> expandosValues = Iterables.transform(ImmutableList.of(Expandos.SCHEMA, Expandos.NAMES),
                EXPANDO_TO_PARAM);
        final String notNullJql = StringUtils.defaultString(jql);
        try {
            if (notNullJql.length() > MAX_JQL_LENGTH_FOR_HTTP_GET) {
                return searchJqlImplPost(maxResults, startAt, expandosValues, notNullJql, fields);
            } else {
                return searchJqlImplGet(maxResults, startAt, expandosValues, notNullJql, fields);
            }
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addOptionalQueryParam(final UriBuilder uriBuilder, final String key, final Object... values) {
        if (values != null && values.length > 0 && values[0] != null) {
            uriBuilder.queryParam(key, values);
        }
    }

    private SearchResult searchJqlImplGet(@Nullable Integer maxResults, @Nullable Integer startAt,
            Iterable<String> expandosValues, String jql, @Nullable Set<String> fields) throws JSONException, IOException {
        final UriBuilder uriBuilder = UriBuilder.fromUri(searchUri).queryParam(JQL_ATTRIBUTE, jql).queryParam(EXPAND_ATTRIBUTE,
                Joiner.on(",").join(expandosValues));

        if (fields != null) {
            uriBuilder.queryParam(FIELDS_ATTRIBUTE, Joiner.on(",").join(fields));
        }
        addOptionalQueryParam(uriBuilder, MAX_RESULTS_ATTRIBUTE, maxResults);
        addOptionalQueryParam(uriBuilder, START_AT_ATTRIBUTE, startAt);

        return getAndParse(uriBuilder.build());
    }

    private SearchResult searchJqlImplPost(@Nullable Integer maxResults, @Nullable Integer startAt,
            Iterable<String> expandosValues, String jql, @Nullable Set<String> fields) throws JSONException, IOException {
        final JSONObject postEntity = new JSONObject();

        postEntity.put(JQL_ATTRIBUTE, jql).put(EXPAND_ATTRIBUTE, ImmutableList.copyOf(expandosValues))
                .putOpt(START_AT_ATTRIBUTE, startAt).putOpt(MAX_RESULTS_ATTRIBUTE, maxResults);

        if (fields != null) {
            postEntity.put(FIELDS_ATTRIBUTE, fields); // putOpt doesn't work with collections
        }

        return postAndParse(searchUri, postEntity);
    }

    private SearchResult postAndParse(final URI uri, final JSONObject postEntity) throws IOException, JSONException {
        final HttpPost request = new HttpPost(uri);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        request.setEntity(new StringEntity(postEntity.toString(), UTF_8));
        try (CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(credsProvider).build()) {

            CloseableHttpResponse response = client.execute(request);
            String json = IOUtils.toString(response.getEntity().getContent());

            // parse json to SearchResult
            return parseAsSearchResult(new JSONObject(json));
        }

    }

    private SearchResult parseAsSearchResult(JSONObject jsonObject) throws JSONException {
        final JSONArray issuesJsonArray = jsonObject.getJSONArray("issues");

        final List<JiraIssue> issues;
        if (issuesJsonArray.length() > 0) {
            issues = parseAsJiraIssues(issuesJsonArray);
        } else {
            issues = Collections.emptyList();
        }
        return new SearchResult(issues);
    }

    private List<JiraIssue> parseAsJiraIssues(JSONArray issuesJsonArray) throws JSONException {
        List<JiraIssue> issues = new ArrayList<>();
        for (int i = 0; i < issuesJsonArray.length(); i++) {
            JSONObject jsonObject = issuesJsonArray.getJSONObject(i);
            issues.add(parseAsJiraIssue(jsonObject));
        }

        return issues;
    }

    public JiraIssue parseAsJiraIssue(JSONObject jsonObject) throws JSONException {
        String type = jsonObject.getJSONObject("fields").getJSONObject("issueType").getString("name");
        String key = jsonObject.getString("key");
        String summary = jsonObject.getJSONObject("fields").getString("summary");
        return new JiraIssue(type, key, summary);
    }

    protected final SearchResult getAndParse(final URI uri) throws IOException, JSONException {
        final HttpGet request = new HttpGet(uri);
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        try (CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(credsProvider).build()) {

            CloseableHttpResponse response = client.execute(request);
            String json = IOUtils.toString(response.getEntity().getContent());

            // parse json to SearchResult
            return parseAsSearchResult(new JSONObject(json));
        }

    }

    protected final JiraIssue getAndParseAsJiraIssue(final URI uri) throws IOException, JSONException {
        final HttpGet request = new HttpGet(uri);
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        try (CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(credsProvider).build()) {

            CloseableHttpResponse response = client.execute(request);
            String json = IOUtils.toString(response.getEntity().getContent());

            // parse json to JiraIssue
            return parseAsJiraIssue(new JSONObject(json));
        }

    }

    @Override
    public JiraIssue getIssue(String jiraId) throws JSONException, IOException {
        final UriBuilder uriBuilder = UriBuilder.fromUri(baseUri);
        uriBuilder.path("issue").path(jiraId);
        return getAndParseAsJiraIssue(uriBuilder.build());
    }

    public static enum Expandos {
        CHANGELOG("changelog"),
        OPERATIONS("operations"),
        SCHEMA("schema"),
        NAMES("names"),
        TRANSITIONS("transitions");

        private final String value;

        private Expandos(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}
