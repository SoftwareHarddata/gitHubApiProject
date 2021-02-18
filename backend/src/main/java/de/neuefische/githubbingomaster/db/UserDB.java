package de.neuefische.githubbingomaster.db;
import de.neuefische.githubbingomaster.model.GitHubUser;
import de.neuefische.githubbingomaster.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDB {

    private List<GitHubUser> userList;

    public UserDB () {
        this.userList = new ArrayList<GitHubUser>();
    }

    public List<GitHubUser> getUsers() {
        return userList;
    }

    public GitHubUser addUser(GitHubUser user) {
        userList.add(user);
        return user;
    }
}
