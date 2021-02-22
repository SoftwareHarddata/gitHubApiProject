package de.neuefische.githubbingomaster.db;

import de.neuefische.githubbingomaster.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

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

    public void clear() {
        users.clear();
    }

    public List<User> list() {
        return Collections.unmodifiableList(users);
    }

    public Optional<User> findByUsername(String username) {
        return users.stream().filter(user ->
                Objects.equals(user.getName(), username))
                .findAny();
    }
}
