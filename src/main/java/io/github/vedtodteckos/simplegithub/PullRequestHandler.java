package io.github.vedtodteckos.simplegithub;

import lombok.RequiredArgsConstructor;
import org.kohsuke.github.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Handler for pull request operations.
 * This class provides methods for managing GitHub pull requests including:
 * - Reviewing and merging
 * - Managing labels and reviewers
 * - Adding comments and review comments
 * - Tracking pull request state
 */
@RequiredArgsConstructor
public class PullRequestHandler {
    private final GHPullRequest pullRequest;

    /**
     * Gets the current state of the pull request.
     *
     * @return The pull request state (OPEN, CLOSED, or MERGED)
     */
    public String getState() {
        return pullRequest.getState().name();
    }

    /**
     * Checks if the pull request has been merged.
     *
     * @return true if the pull request is merged, false otherwise
     * @throws IOException if the GitHub API request fails
     */
    public boolean isMerged() throws IOException {
        return pullRequest.isMerged();
    }

    /**
     * Gets the list of users requested to review this pull request.
     *
     * @return List of GitHub usernames of requested reviewers
     * @throws IOException if the GitHub API request fails
     */
    public List<String> getRequestedReviewers() throws IOException {
        return StreamSupport.stream(pullRequest.getRequestedReviewers().spliterator(), false)
                .map(GHUser::getLogin)
                .collect(Collectors.toList());
    }

    /**
     * Adds labels to the pull request.
     *
     * @param labels One or more label names to add to the pull request
     * @throws IOException if the GitHub API request fails
     */
    public void addLabels(String... labels) throws IOException {
        pullRequest.addLabels(labels);
    }

    /**
     * Removes labels from the pull request.
     *
     * @param labels One or more label names to remove from the pull request
     * @throws IOException if the GitHub API request fails
     */
    public void removeLabels(String... labels) throws IOException {
        for (String label : labels) {
            pullRequest.removeLabel(label);
        }
    }

    /**
     * Merges the pull request using the specified merge method.
     *
     * @param commitMessage The commit message to use for the merge
     * @param mergeMethod The merge method to use (MERGE, SQUASH, or REBASE)
     * @throws IOException if the merge fails or the GitHub API request fails
     */
    public void merge(String commitMessage, GHPullRequest.MergeMethod mergeMethod) throws IOException {
        pullRequest.merge(commitMessage, null, mergeMethod);
    }

    /**
     * Adds a comment to the pull request discussion.
     *
     * @param comment The text content of the comment
     * @throws IOException if the GitHub API request fails
     */
    public void comment(String comment) throws IOException {
        pullRequest.comment(comment);
    }

    /**
     * Gets the list of commit SHA hashes in the pull request.
     *
     * @return List of commit SHA hashes
     * @throws IOException if the GitHub API request fails
     */
    public List<String> getCommits() throws IOException {
        return StreamSupport.stream(pullRequest.listCommits().spliterator(), false)
                .map(commit -> {
                    return commit.getSha();
                })
                .collect(Collectors.toList());
    }

    /**
     * Updates the pull request title and body.
     *
     * @param title The new title for the pull request
     * @param body The new description/body text for the pull request
     * @throws IOException if the GitHub API request fails
     */
    public void update(String title, String body) throws IOException {
        pullRequest.setTitle(title);
        pullRequest.setBody(body);
    }

    /**
     * Gets all review comments on the pull request.
     * Review comments are comments made on specific lines of code.
     *
     * @return List of pull request review comments
     * @throws IOException if the GitHub API request fails
     */
    public List<GHPullRequestReviewComment> getReviewComments() throws IOException {
        return StreamSupport.stream(pullRequest.listReviewComments().spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * Creates a review comment on a specific line of code.
     *
     * @param body The text content of the review comment
     * @param commitId The SHA of the commit being commented on
     * @param path The file path relative to the repository root
     * @param position The line number in the file to comment on
     * @throws IOException if the GitHub API request fails
     */
    public void createReviewComment(String body, String commitId, String path, int position) throws IOException {
        pullRequest.createReviewComment(body, commitId, path, position);
    }
}
