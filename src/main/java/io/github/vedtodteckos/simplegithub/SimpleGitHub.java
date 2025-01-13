package io.github.vedtodteckos.simplegithub;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;

/**
 * Main entry point for the SimpleGitHub API wrapper.
 * Provides simplified access to GitHub API functionality.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleGitHub {
    private GitHub github;

    /**
     * Creates a new SimpleGitHub instance using a GitHub access token.
     *
     * @param token GitHub personal access token
     * @return SimpleGitHub instance
     * @throws IOException if connection fails
     */
    public static SimpleGitHub connect(String token) throws IOException {
        SimpleGitHub simpleGitHub = new SimpleGitHub();
        simpleGitHub.github = new GitHubBuilder()
                .withOAuthToken(token)
                .build();
        return simpleGitHub;
    }

    /**
     * Creates a repository handler for the specified repository.
     *
     * @param owner Repository owner
     * @param name Repository name
     * @return RepositoryHandler instance
     */
    public RepositoryHandler repository(String owner, String name) {
        return new RepositoryHandler(github, owner, name);
    }

    /**
     * Checks if the connection to GitHub is valid.
     *
     * @return true if connected successfully
     */
    public boolean isConnected() {
        try {
            github.checkApiUrlValidity();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
} 