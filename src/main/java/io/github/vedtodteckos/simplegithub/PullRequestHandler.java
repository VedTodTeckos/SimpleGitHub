package io.github.vedtodteckos.simplegithub;

import lombok.RequiredArgsConstructor;
import org.kohsuke.github.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Handler for pull request operations.
 */
@RequiredArgsConstructor
public class PullRequestHandler {
    private final GHPullRequest pullRequest;

    /**
     * Gets the current state of the pull request.
     *
     * @return Pull request state (open, closed, merged)
     */
    public String getState() {
        return pullRequest.getState().name();
    }

    /**
     * Checks if the pull request is merged.
     *
     * @return true if merged
     */
    public boolean isMerged() throws IOException {
        return pullRequest.isMerged();
    }

    /**
     * Gets the list of requested reviewers.
     *
     * @return List of reviewer usernames
     * @throws IOException if the request fails
     */
    public List<String> getRequestedReviewers() throws IOException {
        return StreamSupport.stream(pullRequest.getRequestedReviewers().spliterator(), false)
                .map(GHUser::getLogin)
                .collect(Collectors.toList());
    }

    /**
     * Requests reviews from specified users.
     *
     * @param reviewers List of usernames to request reviews from
     * @throws IOException if the request fails
     */
    public void requestReviewers(List<String> reviewers) throws IOException {
        pullRequest.requestReviewers(reviewers);
    }

    /**
     * Adds labels to the pull request.
     *
     * @param labels Labels to add
     * @throws IOException if the request fails
     */
    public void addLabels(String... labels) throws IOException {
        pullRequest.addLabels(labels);
    }

    /**
     * Removes labels from the pull request.
     *
     * @param labels Labels to remove
     * @throws IOException if the request fails
     */
    public void removeLabels(String... labels) throws IOException {
        for (String label : labels) {
            pullRequest.removeLabel(label);
        }
    }

    /**
     * Merges the pull request.
     *
     * @param commitMessage Commit message for the merge
     * @param mergeMethod Merge method (merge, squash, rebase)
     * @throws IOException if the merge fails
     */
    public void merge(String commitMessage, GHPullRequest.MergeMethod mergeMethod) throws IOException {
        pullRequest.merge(commitMessage, null, mergeMethod);
    }

    /**
     * Adds a comment to the pull request.
     *
     * @param comment Comment text
     * @throws IOException if the request fails
     */
    public void comment(String comment) throws IOException {
        pullRequest.comment(comment);
    }

    /**
     * Gets the list of commits in the pull request.
     *
     * @return List of commit SHA hashes
     * @throws IOException if the request fails
     */
    public List<String> getCommits() throws IOException {
        return StreamSupport.stream(pullRequest.listCommits().spliterator(), false)
                .map(commit -> {
                    try {
                        return commit.getSHA1();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Updates the pull request title and body.
     *
     * @param title New title
     * @param body New body
     * @throws IOException if the update fails
     */
    public void update(String title, String body) throws IOException {
        pullRequest.setTitle(title);
        pullRequest.setBody(body);
    }

    /**
     * Gets the review comments on the pull request.
     *
     * @return List of review comments
     * @throws IOException if the request fails
     */
    public List<GHPullRequestReviewComment> getReviewComments() throws IOException {
        return StreamSupport.stream(pullRequest.listReviewComments().spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * Creates a review comment on a specific line of code.
     *
     * @param body Comment text
     * @param commitId Commit SHA
     * @param path File path
     * @param position Line position
     * @throws IOException if the request fails
     */
    public void createReviewComment(String body, String commitId, String path, int position) throws IOException {
        pullRequest.createReviewComment(body, commitId, path, position);
    }
} 