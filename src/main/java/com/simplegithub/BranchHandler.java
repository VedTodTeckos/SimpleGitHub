package com.simplegithub;

import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHRepository;

import java.io.IOException;

/**
 * Handles operations related to a specific branch in a GitHub repository.
 */
@RequiredArgsConstructor
public class BranchHandler {
    private final GHRepository repository;
    private final String branchName;

    /**
     * Gets the SHA-1 of the branch's HEAD.
     *
     * @return SHA-1 of the branch's HEAD
     * @throws IOException if the branch cannot be accessed
     */
    public String getSha() throws IOException {
        return getBranch().getSHA1();
    }

    /**
     * Checks if this branch is protected.
     *
     * @return true if the branch is protected
     * @throws IOException if the branch cannot be accessed
     */
    public boolean isProtected() throws IOException {
        return getBranch().isProtected();
    }

    /**
     * Gets the name of the branch.
     *
     * @return branch name
     */
    public String getName() {
        return branchName;
    }

    /**
     * Deletes this branch.
     *
     * @throws IOException if the branch cannot be deleted
     */
    public void delete() throws IOException {
        String branchRef = "heads/" + branchName;
        repository.getRef(branchRef).delete();
    }

    /**
     * Gets the underlying GHBranch object.
     *
     * @return GHBranch object
     * @throws IOException if the branch cannot be accessed
     */
    private GHBranch getBranch() throws IOException {
        return repository.getBranch(branchName);
    }
} 