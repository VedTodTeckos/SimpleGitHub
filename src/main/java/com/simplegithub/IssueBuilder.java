package com.simplegithub;

import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueBuilder;

import java.io.IOException;
import java.util.Arrays;

/**
 * Fluent builder for creating GitHub issues.
 */
@RequiredArgsConstructor
public class IssueBuilder {
    private final GHIssueBuilder issueBuilder;

    /**
     * Sets the issue title.
     *
     * @param title Issue title
     * @return this builder
     */
    public IssueBuilder title(String title) {
        issueBuilder.title(title);
        return this;
    }

    /**
     * Sets the issue body/description.
     *
     * @param body Issue body
     * @return this builder
     */
    public IssueBuilder body(String body) {
        issueBuilder.body(body);
        return this;
    }

    /**
     * Adds labels to the issue.
     *
     * @param labels Labels to add
     * @return this builder
     */
    public IssueBuilder labels(String... labels) {
        issueBuilder.label(labels);
        return this;
    }

    /**
     * Adds a single label to the issue.
     *
     * @param label Label to add
     * @return this builder
     */
    public IssueBuilder label(String label) {
        return labels(label);
    }

    /**
     * Assigns the issue to specific users.
     *
     * @param assignees GitHub usernames of assignees
     * @return this builder
     */
    public IssueBuilder assignees(String... assignees) {
        issueBuilder.assignee(assignees[0]);
        if (assignees.length > 1) {
            issueBuilder.assignees(Arrays.copyOfRange(assignees, 1, assignees.length));
        }
        return this;
    }

    /**
     * Creates the issue with the specified parameters.
     *
     * @return Created issue
     * @throws IOException if the issue creation fails
     */
    public GHIssue create() throws IOException {
        return issueBuilder.create();
    }
} 