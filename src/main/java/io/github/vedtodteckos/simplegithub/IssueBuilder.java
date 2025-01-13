package io.github.vedtodteckos.simplegithub;

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
        for (String label : labels) {
            issueBuilder.label(label);
        }
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
     * Creates the issue with the specified parameters.
     *
     * @return Created issue
     * @throws IOException if the issue creation fails
     */
    public GHIssue create() throws IOException {
        return issueBuilder.create();
    }
} 