package de.neuefische.githubbingomaster.db;

import de.neuefische.githubbingomaster.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDb {

    private final List<User> users = new ArrayList<>();

    public boolean hasUser(String name) {
        return users.stream().anyMatch(user -> user.getName().equals(name));
    }

    public User addUser(User user) {
        users.add(user);
        return user;
    }

    public void clear(){
        users.clear();
    }
}
