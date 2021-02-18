package de.neuefische.githubbingomaster.db;
import de.neuefische.githubbingomaster.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDB {

    private List<User> userList;

    public UserDB () {
        this.userList = new ArrayList<User>();
    }

    public List<User> getUsers() {
        return userList;
    }

    public User addUser(User user) {
        userList.add(user);
        return user;
    }
}
