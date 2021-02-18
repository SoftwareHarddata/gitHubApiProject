package de.neuefische.githubbingomaster.services;

import de.neuefische.githubbingomaster.db.UserDB;
import de.neuefische.githubbingomaster.model.GitHubUser;
import de.neuefische.githubbingomaster.model.RequestUser;
import de.neuefische.githubbingomaster.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    UserDB userDb;
    GitHubService gitHubService;

    @Autowired
    public UserService(UserDB userDb, GitHubService gitHubService){
        this.userDb=userDb;
        this.gitHubService = gitHubService;
    }

    public List<GitHubUser> getUsers() {
        return userDb.getUsers();
    }

    public Optional<GitHubUser> getUser(String login) {
        return gitHubService.getUser(login);
    }

    public Optional<GitHubUser> addUser(RequestUser requestUser) {
        Optional<GitHubUser> newUser = gitHubService.getUser(requestUser.getLogin());
        if(newUser.isPresent()) {
            userDb.addUser(newUser.get());
        }
        return newUser;
    }
}
