package de.neuefische.githubbingomaster.db;

import de.neuefische.githubbingomaster.model.PullRequest;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PullRequestsMongoDb extends PagingAndSortingRepository<PullRequest, String> {
    List<PullRequest> findAll();
}
