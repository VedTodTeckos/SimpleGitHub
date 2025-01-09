package com.simplegithub;

import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHIssueBuilder;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;

/**
 * Handles operations related to a specific GitHub repository.
 */
@RequiredArgsConstructor
public class RepositoryHandler {
    private final GitHub github;
    private final String owner;
    private final String name;

    /**
     * Creates a new issue builder for this repository.
     *
     * @return IssueBuilder instance
     * @throws IOException if the repository cannot be accessed
     */
    public IssueBuilder createIssue() throws IOException {
        GHRepository repository = github.getRepository(owner + "/" + name);
        return new IssueBuilder(repository.createIssue());
    }

    /**
     * Gets the repository description.
     *
     * @return Repository description
     * @throws IOException if the repository cannot be accessed
     */
    public String getDescription() throws IOException {
        return github.getRepository(owner + "/" + name).getDescription();
    }

    /**
     * Gets the number of open issues in the repository.
     *
     * @return Number of open issues
     * @throws IOException if the repository cannot be accessed
     */
    public int getOpenIssuesCount() throws IOException {
        return github.getRepository(owner + "/" + name).getOpenIssueCount();
    }

    /**
     * Checks if the repository is private.
     *
     * @return true if the repository is private
     * @throws IOException if the repository cannot be accessed
     */
    public boolean isPrivate() throws IOException {
        return github.getRepository(owner + "/" + name).isPrivate();
    }
} 