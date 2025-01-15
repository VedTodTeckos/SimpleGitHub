package io.github.vedtodteckos.simplegithub;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHWorkflow;
import org.kohsuke.github.GitHub;

import lombok.RequiredArgsConstructor;

/**
 * Handles operations related to a specific GitHub repository.
 * This class provides methods for managing repository resources including:
 * - Issues and Pull Requests
 * - Branches
 * - GitHub Actions Workflows
 * - Repository Secrets
 */
@RequiredArgsConstructor
public class RepositoryHandler {
    private final GitHub github;
    private final String owner;
    private final String name;

    /**
     * Creates a new issue builder for this repository.
     *
     * @param title The title of the issue to create
     * @return IssueBuilder instance for fluent issue creation
     * @throws IOException if the repository cannot be accessed or the issue creation fails
     */
    public IssueBuilder createIssue(String title) throws IOException {
        GHRepository repository = getRepository();
        return new IssueBuilder(repository.createIssue(title));
    }

    /**
     * Gets the repository description.
     *
     * @return The repository description text
     * @throws IOException if the repository cannot be accessed
     */
    public String getDescription() throws IOException {
        return getRepository().getDescription();
    }

    /**
     * Gets the number of open issues in the repository.
     *
     * @return The count of open issues
     * @throws IOException if the repository cannot be accessed
     */
    public int getOpenIssuesCount() throws IOException {
        return getRepository().getOpenIssueCount();
    }

    /**
     * Checks if the repository is private.
     *
     * @return true if the repository is private, false if it's public
     * @throws IOException if the repository cannot be accessed
     */
    public boolean isPrivate() throws IOException {
        return getRepository().isPrivate();
    }

    /**
     * Gets a handler for a specific branch.
     *
     * @param branchName The name of the branch to handle
     * @return BranchHandler instance for the specified branch
     * @throws IOException if the repository or branch cannot be accessed
     */
    public BranchHandler branch(String branchName) throws IOException {
        return new BranchHandler(getRepository(), branchName);
    }

    /**
     * Gets a list of all branch names in the repository.
     *
     * @return List of branch names in the repository
     * @throws IOException if the repository cannot be accessed
     */
    public List<String> getBranchNames() throws IOException {
        return getRepository().getBranches().keySet().stream().collect(Collectors.toList());
    }

    /**
     * Creates a new branch from the specified source branch.
     *
     * @param newBranchName Name of the new branch to create
     * @param sourceBranchName Name of the source branch to branch from
     * @return BranchHandler for the newly created branch
     * @throws IOException if the branch cannot be created or the repository cannot be accessed
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
     * @return The name of the default branch (e.g., "main" or "master")
     * @throws IOException if the repository cannot be accessed
     */
    public String getDefaultBranch() throws IOException {
        return getRepository().getDefaultBranch();
    }

    /**
     * Creates a new pull request in the repository.
     *
     * @param title Title of the pull request
     * @param head Name of the branch containing the changes (source branch)
     * @param base Name of the branch to merge into (target branch)
     * @param body Description/body text of the pull request
     * @return PullRequestHandler for managing the newly created pull request
     * @throws IOException if the pull request cannot be created or the repository cannot be accessed
     */
    public PullRequestHandler createPullRequest(String title, String head, String base, String body) throws IOException {
        GHPullRequest pr = getRepository().createPullRequest(title, head, base, body);
        return new PullRequestHandler(pr);
    }

    /**
     * Gets a handler for an existing pull request.
     *
     * @param number The pull request number
     * @return PullRequestHandler instance for managing the pull request
     * @throws IOException if the pull request cannot be accessed or doesn't exist
     */
    public PullRequestHandler pullRequest(int number) throws IOException {
        return new PullRequestHandler(getRepository().getPullRequest(number));
    }

    /**
     * Lists all open pull requests in the repository.
     *
     * @return List of PullRequestHandler instances for all open pull requests
     * @throws IOException if the repository cannot be accessed
     */
    public List<PullRequestHandler> getOpenPullRequests() throws IOException {
        return StreamSupport.stream(getRepository().queryPullRequests()
                .state(GHIssueState.OPEN)
                .list()
                .spliterator(), false)
                .map(PullRequestHandler::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets a handler for a GitHub Actions workflow.
     *
     * @param workflowId The workflow identifier (can be the filename or workflow ID)
     * @return WorkflowHandler instance for managing the workflow
     * @throws IOException if the repository cannot be accessed
     */
    public WorkflowHandler workflow(String workflowId) throws IOException {
        return new WorkflowHandler(getRepository(), workflowId);
    }

    /**
     * Lists all workflows defined in the repository.
     *
     * @return List of workflow IDs
     * @throws IOException if the repository cannot be accessed
     */
    public List<String> getWorkflows() throws IOException {
        return StreamSupport.stream(getRepository().listWorkflows().spliterator(), false)
                .map(GHWorkflow::getId)
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    /**
     * Creates or updates a repository secret.
     *
     * @param name The name of the secret
     * @param value The value of the secret
     * @param publicKeyId The public key ID for encrypting the secret
     * @throws IOException if the secret cannot be created/updated or the repository cannot be accessed
     */
    public void createSecret(String name, String value, String publicKeyId) throws IOException {
        getRepository().createSecret(name, value, publicKeyId);
    }

    /**
     * Deletes a repository secret.
     *
     * @param name The name of the secret to delete
     * @throws IOException if the secret cannot be deleted or the repository cannot be accessed
     * @throws UnsupportedOperationException if the operation is not implemented
     */
    public void deleteSecret(String name) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Gets the underlying GHRepository object.
     *
     * @return The GitHub API repository object
     * @throws IOException if the repository cannot be accessed
     */
    private GHRepository getRepository() throws IOException {
        return github.getRepository(owner + "/" + name);
    }
} 