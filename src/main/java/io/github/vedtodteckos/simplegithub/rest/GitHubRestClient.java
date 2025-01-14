package io.github.vedtodteckos.simplegithub.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.HttpConnector;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Client for direct GitHub REST API communication.
 * Handles endpoints not covered by the standard GitHub API library.
 */
@RequiredArgsConstructor
public class GitHubRestClient {
    private static final String API_BASE_URL = "https://api.github.com";
    private final String token;
    private final HttpConnector connector;
    private final Gson gson;

    /**
     * Creates a new GitHubRestClient instance.
     *
     * @param token GitHub personal access token
     */
    public GitHubRestClient(String token) throws IOException {
        this.token = token;
        this.connector = GitHub.connectUsingOAuth(token).getConnector();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    /**
     * Gets Copilot seat information for the authenticated user's organization.
     *
     * @param orgName Organization name
     * @return CopilotSeatInfo containing seat allocation details
     * @throws IOException if the request fails
     */
    public CopilotSeatInfo getCopilotSeats(String orgName) throws IOException {
        String response = sendGetRequest("/orgs/" + orgName + "/copilot/billing");
        return gson.fromJson(response, CopilotSeatInfo.class);
    }

    /**
     * Gets Copilot usage metrics for the organization.
     *
     * @param orgName Organization name
     * @param startDate Start date for metrics (inclusive)
     * @param endDate End date for metrics (inclusive)
     * @return CopilotUsageMetrics containing usage statistics
     * @throws IOException if the request fails
     */
    public CopilotUsageMetrics getCopilotUsage(String orgName, LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        String endpoint = String.format("/orgs/%s/copilot/usage?start_date=%s&end_date=%s",
                orgName, startDate.toString(), endDate.toString());
        String response = sendGetRequest(endpoint);
        return gson.fromJson(response, CopilotUsageMetrics.class);
    }

    /**
     * Gets Copilot user assignment status for a specific user.
     *
     * @param orgName Organization name
     * @param username GitHub username
     * @return CopilotUserStatus containing user's Copilot status
     * @throws IOException if the request fails
     */
    public CopilotUserStatus getCopilotUserStatus(String orgName, String username) throws IOException {
        String response = sendGetRequest("/orgs/" + orgName + "/members/" + username + "/copilot");
        return gson.fromJson(response, CopilotUserStatus.class);
    }

    /**
     * Assigns Copilot seat to a user in the organization.
     *
     * @param orgName Organization name
     * @param username GitHub username
     * @throws IOException if the request fails
     */
    public void assignCopilotSeat(String orgName, String username) throws IOException {
        sendPutRequest("/orgs/" + orgName + "/members/" + username + "/copilot", Map.of());
    }

    /**
     * Removes Copilot seat from a user in the organization.
     *
     * @param orgName Organization name
     * @param username GitHub username
     * @throws IOException if the request fails
     */
    public void removeCopilotSeat(String orgName, String username) throws IOException {
        sendDeleteRequest("/orgs/" + orgName + "/members/" + username + "/copilot");
    }

    private String sendGetRequest(String endpoint) throws IOException {
        return sendRequest("GET", endpoint, null);
    }

    private String sendPutRequest(String endpoint, Map<String, Object> body) throws IOException {
        return sendRequest("PUT", endpoint, body);
    }

    private String sendDeleteRequest(String endpoint) throws IOException {
        return sendRequest("DELETE", endpoint, null);
    }

    private String sendRequest(String method, String endpoint, Map<String, Object> body) throws IOException {
        URL url = new URL(API_BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Accept", "application/vnd.github+json");
        conn.setRequestProperty("X-GitHub-Api-Version", "2022-11-28");

        if (body != null) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            String jsonBody = gson.toJson(body);
            conn.getOutputStream().write(jsonBody.getBytes(StandardCharsets.UTF_8));
        }

        try (InputStream is = conn.getInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
} 