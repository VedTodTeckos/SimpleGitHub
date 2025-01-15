package io.github.vedtodteckos.simplegithub;

import lombok.RequiredArgsConstructor;
import org.kohsuke.github.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Handler for GitHub Actions workflow operations.
 * This class provides methods for managing GitHub Actions workflows including:
 * - Triggering workflow runs
 * - Managing workflow state
 * - Accessing workflow runs and jobs
 * - Retrieving workflow logs and timing information
 */
@RequiredArgsConstructor
public class WorkflowHandler {
    private final GHRepository repository;
    private final String workflowId;

    /**
     * Lists all runs of this workflow.
     * The runs are returned in descending order by run number.
     *
     * @return List of workflow runs
     * @throws IOException if the GitHub API request fails
     */
    public List<GHWorkflowRun> getWorkflowRuns() throws IOException {
        return StreamSupport.stream(repository.queryWorkflowRuns()
                .list()
                .spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * Gets the most recent run of this workflow.
     *
     * @return The latest workflow run, or null if no runs exist
     * @throws IOException if the GitHub API request fails
     */
    public GHWorkflowRun getLatestRun() throws IOException {
        List<GHWorkflowRun> runs = getWorkflowRuns();
        return runs.isEmpty() ? null : runs.get(0);
    }

    /**
     * Triggers a new workflow run.
     *
     * @param branch The branch to run the workflow on
     * @param inputs Map of input parameters for the workflow
     * @throws IOException if the GitHub API request fails
     */
    public void dispatch(String branch, Map<String, Object> inputs) throws IOException {
        repository.getWorkflow(workflowId)
                .dispatch(branch, inputs);
    }

    /**
     * Enables or disables the workflow.
     * Disabled workflows cannot be triggered manually or automatically.
     *
     * @param enabled true to enable the workflow, false to disable it
     * @throws IOException if the GitHub API request fails
     */
    public void setEnabled(boolean enabled) throws IOException {
        GHWorkflow workflow = repository.getWorkflow(workflowId);
        if (enabled) {
            workflow.enable();
        } else {
            workflow.disable();
        }
    }

    /**
     * Cancels a running workflow run.
     *
     * @param runId The ID of the workflow run to cancel
     * @throws IOException if the GitHub API request fails or the run cannot be cancelled
     */
    public void cancelRun(long runId) throws IOException {
        repository.getWorkflowRun(runId).cancel();
    }

    /**
     * Re-runs all failed jobs in a workflow run.
     *
     * @param runId The ID of the workflow run containing the failed jobs
     * @throws IOException if the GitHub API request fails or the jobs cannot be re-run
     */
    public void rerunFailedJobs(long runId) throws IOException {
        repository.getWorkflowRun(runId).rerun();
    }

    /**
     * Gets the URL for downloading the logs of a workflow run.
     *
     * @param runId The ID of the workflow run
     * @return URL string for downloading the logs
     * @throws IOException if the GitHub API request fails
     */
    public String getLogsUrl(long runId) throws IOException {
        return repository.getWorkflowRun(runId).getLogsUrl().toString();
    }

    /**
     * Gets all jobs from a specific workflow run.
     * This includes both completed and in-progress jobs.
     *
     * @param runId The ID of the workflow run
     * @return List of workflow jobs
     * @throws IOException if the GitHub API request fails
     */
    public List<GHWorkflowJob> getJobs(long runId) throws IOException {
        return StreamSupport.stream(repository.getWorkflowRun(runId)
                .listJobs()
                .spliterator(), false)
                .collect(Collectors.toList());
    }

} 