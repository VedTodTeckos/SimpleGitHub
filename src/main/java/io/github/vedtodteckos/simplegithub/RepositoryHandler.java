package io.github.vedtodteckos.simplegithub;

import lombok.RequiredArgsConstructor;
import org.kohsuke.github.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        GHRepository repository = getRepository();
        return new IssueBuilder(repository.createIssue());
    }

    /**
     * Gets the repository description.
     *
     * @return Repository description
     * @throws IOException if the repository cannot be accessed
     */
    public String getDescription() throws IOException {
        return getRepository().getDescription();
    }

    /**
     * Gets the number of open issues in the repository.
     *
     * @return Number of open issues
     * @throws IOException if the repository cannot be accessed
     */
    public int getOpenIssuesCount() throws IOException {
        return getRepository().getOpenIssueCount();
    }

    /**
     * Checks if the repository is private.
     *
     * @return true if the repository is private
     * @throws IOException if the repository cannot be accessed
     */
    public boolean isPrivate() throws IOException {
        return getRepository().isPrivate();
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
     * Creates a new pull request.
     *
     * @param title Title of the pull request
     * @param head Source branch
     * @param base Target branch
     * @param body Description of the pull request
     * @return PullRequestHandler for the new pull request
     * @throws IOException if the pull request cannot be created
     */
    public PullRequestHandler createPullRequest(String title, String head, String base, String body) throws IOException {
        GHPullRequest pr = getRepository().createPullRequest(title, head, base, body);
        return new PullRequestHandler(pr);
    }

    /**
     * Gets a handler for an existing pull request.
     *
     * @param number Pull request number
     * @return PullRequestHandler instance
     * @throws IOException if the pull request cannot be accessed
     */
    public PullRequestHandler pullRequest(int number) throws IOException {
        return new PullRequestHandler(getRepository().getPullRequest(number));
    }

    /**
     * Lists all open pull requests.
     *
     * @return List of PullRequestHandler instances
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
     * @param workflowId Workflow ID or filename
     * @return WorkflowHandler instance
     */
    public WorkflowHandler workflow(String workflowId) {
        return new WorkflowHandler(getRepository(), workflowId);
    }

    /**
     * Lists all workflows in the repository.
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
     * @param name Secret name
     * @param value Secret value
     * @throws IOException if the secret cannot be created/updated
     */
    public void createSecret(String name, String value) throws IOException {
        getRepository().createSecret(name, value);
    }

    /**
     * Deletes a repository secret.
     *
     * @param name Secret name
     * @throws IOException if the secret cannot be deleted
     */
    public void deleteSecret(String name) throws IOException {
        getRepository().deleteSecret(name);
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