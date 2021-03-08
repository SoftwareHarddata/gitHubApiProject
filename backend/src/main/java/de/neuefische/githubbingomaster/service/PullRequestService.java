package de.neuefische.githubbingomaster.service;

import de.neuefische.githubbingomaster.githubapi.model.GitHubPullRequest;
import de.neuefische.githubbingomaster.githubapi.service.GitHubApiService;
import de.neuefische.githubbingomaster.model.PullRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PullRequestService {
    private final GitHubApiService gitHubApiService;

    @Autowired
    public PullRequestService(GitHubApiService gitHubApiService) {
        this.gitHubApiService = gitHubApiService;
    }

    public List<PullRequest> getPullRequests(String username, String repositoryName) {
        List<GitHubPullRequest> gitHubPullRequestList = gitHubApiService.getUserRepoPullRequests(username, repositoryName);
        List<PullRequest> pullRequestList = gitHubPullRequestList.stream()
                .map( gitHubPullRequest -> PullRequest.builder()
                        .id(gitHubPullRequest.getId())
                        .pullRequestTitle(gitHubPullRequest.getPullRequestTitle())
                        .pullRequestNumber(gitHubPullRequest.getPullRequestNumber())
                        .pullRequestUrl(gitHubPullRequest.getPullRequestUrl())
                        .repoOwner(gitHubPullRequest.getRepoOwner())
                        .repoName(gitHubPullRequest.getRepoName())
                        .build())
                .collect(Collectors.toList());
        return pullRequestList;
    }
}
