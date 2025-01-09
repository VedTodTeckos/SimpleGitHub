package com.simplegithub;

import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHIssueBuilder;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * Gets a handler for a specific branch.
     *
     * @param branchName Name of the branch
     * @return BranchHandler instance
     * @throws IOException if the repository cannot be accessed
     */
    public BranchHandler branch(String branchName) throws IOException {
        return new BranchHandler(getRepository(), branchName);
    }

    /**
     * Gets a list of all branch names in the repository.
     *
     * @return List of branch names
     * @throws IOException if the repository cannot be accessed
     */
    public List<String> getBranchNames() throws IOException {
        return getRepository().getBranches().keySet().stream().collect(Collectors.toList());
    }

    /**
     * Creates a new branch from the specified source branch.
     *
     * @param newBranchName Name of the new branch
     * @param sourceBranchName Name of the source branch
     * @return BranchHandler for the new branch
     * @throws IOException if the branch cannot be created
     */
    public BranchHandler createBranch(String newBranchName, String sourceBranchName) throws IOException {
        GHRepository repo = getRepository();
        GHBranch sourceBranch = repo.getBranch(sourceBranchName);
        repo.createRef("refs/heads/" + newBranchName, sourceBranch.getSHA1());
        return new BranchHandler(repo, newBranchName);
    }

    /**
     * Gets the default branch name of the repository.
     *
     * @return Default branch name
     * @throws IOException if the repository cannot be accessed
     */
    public String getDefaultBranch() throws IOException {
        return getRepository().getDefaultBranch();
    }

    /**
     * Gets the underlying GHRepository object.
     *
     * @return GHRepository object
     * @throws IOException if the repository cannot be accessed
     */
    private GHRepository getRepository() throws IOException {
        return github.getRepository(owner + "/" + name);
    }
} 