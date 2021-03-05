package de.neuefische.githubbingomaster.service;

import de.neuefische.githubbingomaster.db.WatchlistMongoDb;
import de.neuefische.githubbingomaster.githubapi.model.GitHubRepo;
import de.neuefische.githubbingomaster.githubapi.service.GitHubApiService;
import de.neuefische.githubbingomaster.model.UserRepository;
import de.neuefische.githubbingomaster.model.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WatchListService {
    private final GitHubApiService gitHubApiService;
    private final WatchlistMongoDb watchlistDb;

    @Autowired
    public WatchListService(GitHubApiService gitHubApiService, WatchlistMongoDb watchlistDb) {
        this.gitHubApiService = gitHubApiService;
        this.watchlistDb = watchlistDb;
    }

    public Optional<UserRepository> addToWatchlist(String username, String repositoryName) {
        Optional<GitHubRepo> githubRepo = gitHubApiService.getRepository(username,repositoryName);

        if(githubRepo.isEmpty()){
            return  Optional.empty();
        }
        WatchlistRepository watchlistRepository = WatchlistRepository.builder()
                .id(githubRepo.get().getId())
                .repositoryName(githubRepo.get().getRepository())
                .repositoryWebUrl(githubRepo.get().getRepositoryUrl())
                .build();

        UserRepository userRepository = mapWatchListToUserRepo(watchlistRepository);

        if (watchlistDb.existsById(userRepository.getId())) {
            return Optional.of(userRepository);
        }


        watchlistDb.save(watchlistRepository);

        return Optional.of(userRepository);
    }

    private UserRepository mapWatchListToUserRepo(WatchlistRepository watchlistRepository) {
        return UserRepository.builder()
                .id(watchlistRepository.getId())
                .repositoryName(watchlistRepository.getRepositoryName())
                .repositoryWebUrl(watchlistRepository.getRepositoryWebUrl())
                .onWatchlist(true)
                .build();
    }

    public void deleteFromWatchlist(String id) {
        watchlistDb.deleteById(id);
    }

    public List<UserRepository> getWatchlist(){
        return watchlistDb.findAll().stream()
                .map(this::mapWatchListToUserRepo)
                .collect(Collectors.toList());
    }
}
