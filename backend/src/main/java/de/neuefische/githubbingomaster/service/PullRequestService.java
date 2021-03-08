package de.neuefische.githubbingomaster.service;

import de.neuefische.githubbingomaster.db.PullRequestsMongoDb;
import de.neuefische.githubbingomaster.githubapi.service.GitHubApiService;
import de.neuefische.githubbingomaster.model.PullRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PullRequestService {
    private final GitHubApiService gitHubApiService;
    private final PullRequestsMongoDb pullRequestsDb;

    @Autowired
    public PullRequestService(GitHubApiService gitHubApiService, PullRequestsMongoDb pullRequestsDb) {
        this.gitHubApiService = gitHubApiService;
        this.pullRequestsDb = pullRequestsDb;
    }

    public List<PullRequest> getPullRequests(String username, String repositoryName) {
        return List.of();
    }
}
