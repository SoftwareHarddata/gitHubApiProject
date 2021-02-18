package de.neuefische.githubbingomaster.services;

import de.neuefische.githubbingomaster.db.UserDB;
import de.neuefische.githubbingomaster.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GitHubService {

    UserDB userDb;
    ApiService apiService;

    @Autowired
    public GitHubService(UserDB userDb,ApiService apiService){
        this.userDb=userDb;
        this.apiService=apiService;
    }

    public List<User> getUsers() {
        return userDb.getUsers();
    }

    public Optional<User> getUser(int id) {
        return apiService.getUser(id);
    }

    public User addUser(User user) {
        return userDb.addUser(user);
    }
}
