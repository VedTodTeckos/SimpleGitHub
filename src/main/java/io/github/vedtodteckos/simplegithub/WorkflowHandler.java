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
 */
@RequiredArgsConstructor
public class WorkflowHandler {
    private final GHRepository repository;
    private final String workflowId;

    /**
     * Lists all workflow runs.
     *
     * @return List of workflow runs
     * @throws IOException if the request fails
     */
    public List<GHWorkflowRun> getWorkflowRuns() throws IOException {
        return StreamSupport.stream(repository.queryWorkflowRuns()
                .workflow(workflowId)
                .list()
                .spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * Gets the latest workflow run.
     *
     * @return Latest workflow run or null if none exists
     * @throws IOException if the request fails
     */
    public GHWorkflowRun getLatestRun() throws IOException {
        List<GHWorkflowRun> runs = getWorkflowRuns();
        return runs.isEmpty() ? null : runs.get(0);
    }

    /**
     * Triggers a workflow run.
     *
     * @param branch Branch to run the workflow on
     * @param inputs Input parameters for the workflow
     * @throws IOException if the request fails
     */
    public void dispatch(String branch, Map<String, String> inputs) throws IOException {
        repository.getWorkflow(workflowId)
                .dispatch(branch, inputs);
    }

    /**
     * Enables or disables the workflow.
     *
     * @param enabled true to enable, false to disable
     * @throws IOException if the request fails
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
     * Gets the workflow usage statistics.
     *
     * @return Workflow usage statistics
     * @throws IOException if the request fails
     */
    public GHWorkflowUsage getUsage() throws IOException {
        return repository.getWorkflow(workflowId).getUsage();
    }

    /**
     * Cancels a workflow run.
     *
     * @param runId Workflow run ID
     * @throws IOException if the request fails
     */
    public void cancelRun(long runId) throws IOException {
        repository.getWorkflowRun(runId).cancel();
    }

    /**
     * Re-runs a failed workflow run.
     *
     * @param runId Workflow run ID
     * @throws IOException if the request fails
     */
    public void rerunFailedJobs(long runId) throws IOException {
        repository.getWorkflowRun(runId).rerunFailedJobs();
    }

    /**
     * Gets the logs URL for a workflow run.
     *
     * @param runId Workflow run ID
     * @return URL to download the logs
     * @throws IOException if the request fails
     */
    public String getLogsUrl(long runId) throws IOException {
        return repository.getWorkflowRun(runId).getLogsUrl();
    }

    /**
     * Gets the jobs for a workflow run.
     *
     * @param runId Workflow run ID
     * @return List of jobs
     * @throws IOException if the request fails
     */
    public List<GHWorkflowJob> getJobs(long runId) throws IOException {
        return StreamSupport.stream(repository.getWorkflowRun(runId)
                .listJobs()
                .spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * Gets the timing information for a workflow run.
     *
     * @param runId Workflow run ID
     * @return Timing information
     * @throws IOException if the request fails
     */
    public GHWorkflowRun.Timing getTiming(long runId) throws IOException {
        return repository.getWorkflowRun(runId).getTiming();
    }
} 