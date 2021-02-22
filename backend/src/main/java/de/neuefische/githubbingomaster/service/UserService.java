package de.neuefische.githubbingomaster.service;

import de.neuefische.githubbingomaster.db.UserDb;
import de.neuefische.githubbingomaster.githubapi.model.GitHubProfile;
import de.neuefische.githubbingomaster.githubapi.service.GitHubApiService;
import de.neuefische.githubbingomaster.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final GitHubApiService gitHubApiService;
    private final UserDb userDb;

    @Autowired
    public UserService(GitHubApiService gitHubApiService, UserDb userDb) {
        this.gitHubApiService = gitHubApiService;
        this.userDb = userDb;
    }

    public User addUser(String name) {
        Optional<GitHubProfile> optionalProfile = gitHubApiService.getUserprofile(name);
        if(optionalProfile.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + name + " is not a GitHub user");
        }
        if(userDb.hasUser(name)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " +  name + " is already in the database");
        }
        GitHubProfile profile = optionalProfile.get();
        User user = User.builder().name(profile.getLogin()).avatar(profile.getAvatarUrl()).build();
        return userDb.addUser(user);
    }

    public List<User> listUsers() {
        return userDb.list();
    }
}
