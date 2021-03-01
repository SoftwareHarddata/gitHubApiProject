package de.neuefische.githubbingomaster.service;

import de.neuefische.githubbingomaster.db.UserMongoDb;
import de.neuefische.githubbingomaster.db.WatchlistMongoDb;
import de.neuefische.githubbingomaster.githubapi.model.GitHubProfile;
import de.neuefische.githubbingomaster.githubapi.service.GitHubApiService;
import de.neuefische.githubbingomaster.model.User;
import de.neuefische.githubbingomaster.model.UserRepository;
import de.neuefische.githubbingomaster.model.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final GitHubApiService gitHubApiService;
    private final UserMongoDb userDb;
    private final WatchlistMongoDb watchlistDb;

    @Autowired
    public UserService(GitHubApiService gitHubApiService, UserMongoDb userDb, WatchlistMongoDb watchlistDb) {
        this.gitHubApiService = gitHubApiService;
        this.userDb = userDb;
        this.watchlistDb = watchlistDb;
    }

    public User addUser(String name) {
        Optional<GitHubProfile> optionalProfile = gitHubApiService.getUserprofile(name);
        if (optionalProfile.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + name + " is not a GitHub user");
        }

        if (userDb.existsById(name)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + name + " is already in the database");
        }
        GitHubProfile profile = optionalProfile.get();

        User user = User.builder().name(profile.getLogin()).avatar(profile.getAvatarUrl()).build();
        return userDb.save(user);
    }

    public List<User> listUsers() {
        return userDb.findAll();
    }

    public Optional<User> getUserByUsername(String username) {
        return userDb.findById(username);
    }

    public Optional<List<UserRepository>> getRepositories(String name) {
        if (userDb.existsById(name)) {
            return Optional.of(gitHubApiService.getUserRepos(name).stream()
                    .map(githubRepo -> UserRepository.builder()
                            .id(githubRepo.getId())
                            .repositoryName(githubRepo.getRepository())
                            .repositoryWebUrl(githubRepo.getRepositoryUrl())
                            .onWatchlist(watchlistDb.existsById(githubRepo.getRepositoryUrl()))
                            .build())
                    .collect(Collectors.toList()));
        }
        return Optional.empty();
    }

    public Optional<UserRepository> addToWatchlist(UserRepository userRepository, String username) {
        if (watchlistDb.existsById(userRepository.getId())) {
            return Optional.empty();
        }

        String avatarUrl = getUserByUsername(username).orElse(new User()).getAvatar();

        WatchlistRepository watchlistRepository = WatchlistRepository.builder()
                .id(userRepository.getId())
                .repositoryName(userRepository.getRepositoryName())
                .repositoryWebUrl(userRepository.getRepositoryWebUrl())
                .avatarUrl(avatarUrl)
                .build();

        watchlistDb.save(watchlistRepository);
        userRepository.setOnWatchlist(true);

        return Optional.of(userRepository);
    }

    public void deleteFromWatchlist(String id) {
        watchlistDb.deleteById(id);
    }

    public List<WatchlistRepository> getWatchlist(){
        return watchlistDb.findAll();
    }
}
