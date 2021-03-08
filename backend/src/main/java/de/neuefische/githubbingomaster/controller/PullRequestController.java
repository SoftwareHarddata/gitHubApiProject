package de.neuefische.githubbingomaster.controller;

import de.neuefische.githubbingomaster.model.PullRequest;
import de.neuefische.githubbingomaster.service.PullRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/pullrequests")
public class PullRequestController {

    private final PullRequestService pullRequestService;

    @Autowired
    public PullRequestController(PullRequestService pullRequestService) {
        this.pullRequestService = pullRequestService;
    }

    @GetMapping("{username}/{repositoryName}")
    public List<PullRequest> getPullRequests(@PathVariable String username, @PathVariable String repositoryName) {
        return pullRequestService.getPullRequests(username, repositoryName);
    }
}
