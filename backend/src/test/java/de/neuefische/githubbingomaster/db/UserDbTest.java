package de.neuefische.githubbingomaster.db;

import de.neuefische.githubbingomaster.model.User;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

class UserDbTest {

    private final UserDb userDb = new UserDb();

    @Test
    @DisplayName("Add should insert users into list")
    public void addUser() {
        //GIVEN
        User user = new User("supergithubuser", "someavatar");

        //WHEN
        User userResponse = userDb.addUser(user);
        List<User> users = userDb.list();

        //THEN
        assertThat(userResponse, Matchers.is(new User("supergithubuser", "someavatar")));
        assertThat(users, contains(new User("supergithubuser", "someavatar")));
    }

}
